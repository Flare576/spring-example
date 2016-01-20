package com.flare576.restCountries.service;

import com.flare576.restCountries.io.Country;
import com.flare576.restCountries.io.QueryResult;
import com.flare576.restCountries.io.RestCountriesProxy;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Flare576 on 1/18/2016.
 */
public class QueryService {
    private static Map<String, QueryResult> resultsCache;
    RestCountriesProxy restCountriesProxy;

    @Autowired
    public QueryService(RestCountriesProxy restCountriesProxy){
        resultsCache = new HashMap<>();
        this.restCountriesProxy = restCountriesProxy;
    }

    public Country[] getCountriesByAlpha3( String codes){
        List<Country> mapped = new ArrayList<>();
        String[] codesA = codes.split(",");
        Map<String, Country> countries = restCountriesProxy.getCountries();
        for(String code : codesA){
            mapped.add(countries.get(code));
        }
        return mapped.toArray(new Country[mapped.size()]);
    }

    public Country[] getByBorderCount( Integer qty){
        return getByFieldCount("getBorders", qty);
    }

    public Country[] getByMaxBorderCount(){
        return getByMaxFieldCount( "getBorders" );
    }

    public Integer getMaxBorderCount(){
        return getMaxFieldCount("getBorders");
    }

    public Country[] getByTimezoneCount( Integer qty){
        return getByFieldCount("getTimezones", qty);
    }

    public Country[] getByMaxTimezoneCount(){
        return getByMaxFieldCount("getTimeZones");
    }

    public Integer getMaxTimezoneCount(){
        return getMaxFieldCount("getTimeZones");
    }

    /*
     * The following private methods use Reflection to hit the appropriate methods of the Country class.
     * This means they are flexible, but it also means they are slow. In order to mitigate the performance hit,
     * they also use a caching system that is aware of the RestCountriesProxy, described on refreshFieldStats
     */

    /**
     * Returns an array of Country objects which have qty number of bordering countries.
     *
     * @param methodName String representation of the method inside of Country to call, e.g., getBorders or getTimeZones
     * @param qty The qty you're interested in
     * @return an array of Countries with the requested qty of entries for the methodName.
     */
    private Country[] getByFieldCount( String methodName, Integer qty ){
        refreshFieldStats(methodName);
        String cacheName = "qty_" + methodName + "_" + qty;
        QueryResult cached = resultsCache.get(cacheName);
        Map<String, Country> countries = restCountriesProxy.getCountries();
        //If there's no cache object for the qty, it means there were no countries with the requested qty
        return cached != null ? cached.getSubSet(countries) : new Country[0];
    }

    /**
     * Returns the maximum qty a field reached in the Country list
     *
     * @param methodName String representation of the method inside of Country to call, e.g., getBorders or getTimeZones
     * @return maximum qty found
     */
    private Integer getMaxFieldCount(String methodName) {
        refreshFieldStats(methodName);
        String cacheName = "max_qty_" + methodName;
        return resultsCache.get(cacheName).getIntVal();
    }

    /**
     * Using the max_qty result and the array held in it's cache position, return the Countries with the most bordering
     * Countries.
     *
     * @param methodName String representation of the method inside of Country to call, e.g., getBorders or getTimeZones
     * @return
     */
    private Country[] getByMaxFieldCount( String methodName ){
        refreshFieldStats(methodName);
        String cacheName = "qty_" + methodName + "_" + getMaxFieldCount(methodName);
        Map<String, Country> countries = restCountriesProxy.getCountries();
        return resultsCache.get(cacheName).getSubSet(countries);
    }

    /**
     * Checks a known results cache entry for its lastRun value, then compares it against the Proxy's lastUpdate.
     * If the LastRun is older than the result set, the stats need to be re-run.
     *
     * @param methodName String representation of the method inside of Country to call, e.g., getBorders or getTimeZones
     */
    private void refreshFieldStats(String methodName){
        Map<String, Country> countries = restCountriesProxy.getCountries();
        //this key will always be set on a refresh, so is a good indicator of the age of the cache
        String cacheName = "max_qty_" + methodName;
        QueryResult cached = resultsCache.get(cacheName);
        if(null == cached || cached.getLastRun().before(restCountriesProxy.getLastUpdate())) {
            //Build a result set for each qty.
            Map<Integer, List<Country>> resultSet = new HashMap<>();
            //And track the maximum Quantity found
            int maximumQty = 0;
            try {
                Method method = Country.class.getMethod(methodName);
                //todo solve ConcurrentModificationException
                for (Country country : countries.values()) {
                    int length = ((Object[]) method.invoke(country)).length;
                    if(!resultSet.containsKey(length)){
                        resultSet.put(length,new ArrayList<Country>());
                    }
                    resultSet.get(length).add(country);
                    maximumQty = Math.max(maximumQty, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Insert our max count
            cacheName = "max_qty_" + methodName;
            resultsCache.put(cacheName, new QueryResult(maximumQty));

            //Insert each of the known lengths in their own cache entry for fast access upon request
            for(Integer length: resultSet.keySet()){
                QueryResult temp = new QueryResult(resultSet.get(length));
                cacheName = "qty_" + methodName + "_" + length;
                resultsCache.put(cacheName, temp);
            }
        }
    }
}
