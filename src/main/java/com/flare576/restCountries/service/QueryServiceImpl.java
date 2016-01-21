package com.flare576.restCountries.service;

import com.flare576.restCountries.model.Country;
import com.flare576.restCountries.io.QueryResult;
import com.flare576.restCountries.io.RestCountriesProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Flare576 on 1/18/2016.
 */
public class QueryServiceImpl implements QueryService {
    private Log log = LogFactory.getLog(QueryServiceImpl.class);
    private static Map<String, QueryResult> resultsCache;
    private static final String MAX_PREFACE = "max_qty_";
    private static final String QTY_PREFACE = "qty_";
    private static final String BORDERS = "getBorders"; 
    private static final String TIMEZONES = "getTimezones";
    private static Map<String, Boolean> isRefreshing;
    RestCountriesProxy restCountriesProxy;

    @Autowired
    public QueryServiceImpl(RestCountriesProxy restCountriesProxy){
        resultsCache = new HashMap<>();
        isRefreshing = new HashMap<>();
        this.restCountriesProxy = restCountriesProxy;
    }

    @Override
    public Country[] getCountriesByAlpha3(String codes){
        List<Country> mapped = new ArrayList<>();
        String[] codesA = codes.split(",");
        Map<String, Country> countries = restCountriesProxy.getCountries();
        for(String code : codesA){
            if(countries.containsKey(code)) {
                mapped.add(countries.get(code));
            }
        }
        return mapped.toArray(new Country[mapped.size()]);
    }

    @Override
    public Country[] getByBorderCount(Integer qty){
        return getByFieldCount(BORDERS, qty);
    }

    @Override
    public Country[] getByMaxBorderCount(){
        return getByMaxFieldCount( BORDERS );
    }

    @Override
    public Integer getMaxBorderCount(){
        return getMaxFieldCount(BORDERS);
    }

    @Override
    public Country[] getByTimezoneCount(Integer qty){
        return getByFieldCount(TIMEZONES, qty);
    }

    @Override
    public Country[] getByMaxTimezoneCount(){
        return getByMaxFieldCount(TIMEZONES);
    }

    @Override
    public Integer getMaxTimezoneCount(){
        return getMaxFieldCount(TIMEZONES);
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
        String cacheName = QTY_PREFACE + methodName + "_" + qty;
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
        String cacheName = MAX_PREFACE + methodName;
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
        String cacheName = QTY_PREFACE + methodName + "_" + getMaxFieldCount(methodName);
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
        String cacheCheck = MAX_PREFACE + methodName;
        QueryResult cached = resultsCache.get(cacheCheck);
        if(null == cached || cached.getLastRun().before(restCountriesProxy.getLastUpdate())) {
            if (isRefreshing.containsKey(cacheCheck)) {
                while (isRefreshing.containsKey(cacheCheck)) {
                    log.info("Waiting for cache for " + cacheCheck);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                isRefreshing.put(cacheCheck,true);
                log.info("Refreshing cache for " + cacheCheck);
                //Build a result set for each qty.
                Map<Integer, List<Country>> resultSet = new HashMap<>();
                //And track the maximum Quantity found
                int maximumQty = 0;
                try {
                    Method method = Country.class.getMethod(methodName);
                    for (Country country : countries.values()) {
                        Object[] data = (Object[]) method.invoke(country);
                        int length = 0;
                        if (null != data) {
                            length = data.length;
                        }
                        if (!resultSet.containsKey(length)) {
                            resultSet.put(length, new ArrayList<Country>());
                        }
                        resultSet.get(length).add(country);
                        maximumQty = Math.max(maximumQty, length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Insert our max count
                String cacheName = MAX_PREFACE + methodName;
                resultsCache.put(cacheName, new QueryResult(maximumQty));

                //Insert each of the known lengths in their own cache entry for fast access upon request
                for (Integer length : resultSet.keySet()) {
                    QueryResult temp = new QueryResult(resultSet.get(length));
                    cacheName = QTY_PREFACE + methodName + "_" + length;
                    resultsCache.put(cacheName, temp);
                }
                isRefreshing.remove(cacheCheck);
            }
        }
    }
}
