package com.flare576.restCountries.io;

import com.flare576.restCountries.model.Country;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Flare576 on 1/18/2016.
 *
 * Instead of holding the actual result set, which would be an unnecessary use of memory, we simply store the keys
 * to the Countries in the result. Additionally, the lastRun time is also captured upon creation, allowing comparison
 * to the primary cache's lastUpdate.
 *
 */
public class QueryResult {
    private Date lastRun;
    private String[] keys;
    private Integer intVal;
    private String stringVal;

    public QueryResult(){
        this.lastRun = new Date();
        keys = null;
        intVal = null;
        stringVal = null;
    }

    public QueryResult(List<Country> results){
        this();
        keys = new String[results.size()];
        int i = 0;
        for(Country country: results){
            this.keys[i++] = country.getAlpha3Code();
        }
    }

    public QueryResult(Integer intVal){
        this();
        this.intVal = intVal;
    }

    public QueryResult(String stringVal){
        this();
        this.stringVal = stringVal;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public Country[] getSubSet(Map<String, Country> countries){
        Country[] returnable = new Country[this.keys.length];
        int i = 0;
        for(String key: this.keys){
            returnable[i++] = countries.get(key);
        }
        return returnable;
    }

    public String getStringVal(){
        return stringVal;
    }

    public Integer getIntVal(){
        return intVal;
    }
}
