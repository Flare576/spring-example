package com.flare576.restCountries.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flare576.restCountries.model.Country;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Flare576 on 1/18/2016.
 *
 * This class serves as the provider of the full Countries list from the external URL provided in the properties file.
 * It maintains a cache of the list locally for a configurable period of time for two primary reasons:
 * 1. Country data is unlikely to change rapidly
 * 2. The only known provider for the data is a donation-driven website whose bandwidth is unknown
 */
public class RestCountriesProxy {
    private Log log = LogFactory.getLog(RestCountriesProxy.class);
    private CloseableHttpClient httpClient;
    @Value("${proxy.cache.minutes}")
    private String cacheMinutes;
    @Value("${proxy.external.url}")
    private String restCountriesUrl;

    private Date lastUpdate;
    private Map<String, Country> countries;
    private ObjectMapper objectMapper;


    @Autowired
    public RestCountriesProxy(CloseableHttpClient httpClient){
        this.httpClient = httpClient;

        this.countries = new ConcurrentHashMap<>();
        lastUpdate = null;
        objectMapper = new ObjectMapper();
    }

    /**
     * This is the primary access point to the Proxy. It will check the cache, refresh if necessary, and return the map
     * @return Map of Country.alpha3Code to Country object
     */
    public Map<String, Country> getCountries(){
        if(countries.isEmpty() || cacheExpired()){
            refresh();
        }
        return countries;
    }

    /**
     * In order to allow the queryService to recognize when we've pulled down new data, we need to expose lastUpdate
     * @return The Date representing the last time the cache was refreshed.
     */
    public Date getLastUpdate(){
        return lastUpdate;
    }

    /**
     * Checks to see if the lastUpdate time is older than allowed
     * @return true if the cache has expired, false otherwise
     */
    private boolean cacheExpired(){
        /*
         * I often use "Yoda notation" for the reasons here ( https://en.wikipedia.org/wiki/Yoda_conditions ),
         * however, I'm perfectly happy to conform to standard notation
         */
        if(null == lastUpdate){
            return true;
        }
        Calendar modified = Calendar.getInstance();
        int cacheMinutes = Integer.parseInt(this.cacheMinutes);
        modified.add(Calendar.MINUTE,-cacheMinutes);
        Date cutoff = new Date(modified.getTimeInMillis());
        return lastUpdate.before(cutoff);
    }

    /**
     * Clears the current countries container and refreshes from server
     */
    private void refresh(){
        try{
            HttpGet get = new HttpGet(restCountriesUrl + "/v1/all");
            CloseableHttpResponse response = httpClient.execute(get);
            String body = EntityUtils.toString(response.getEntity(),"UTF-8");
            Country[] countries = objectMapper.readValue(body, Country[].class);
            response.close();
            //Wait for confirmation that the external URL is available before destroying our current cache.
            this.countries.clear();
            for (Country country : countries) {
                this.countries.put(country.getAlpha3Code(), country);
            }
            lastUpdate = new Date();
        } catch (Exception e) {
            log.warn(e);
        }
    }
}
