package com.flare576.restCountries.model;

import java.util.Map;

/**
 * Created by Flare576 on 1/18/2016.
 */
public class Country {
    private String name;
    private String capital;
    private String region;
    private String subregion;
    private String demonym;
    private String nativeName;
    private String alpha2Code;
    private String alpha3Code;
    private String relevance;

    private String[] altSpellings;
    private String[] timezones;
    private String[] borders;
    private String[] callingCodes;
    private String[] topLevelDomain;
    private String[] currencies;
    private String[] languages;

    private Map translations;

    private int population; //If China keeps growing, we'll need to use BigInteger
    private int area; //Km^2
    private int gini;

    private int[] latlng;

    public Country(){}

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getDemonym() {
        return demonym;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public String[] getAltSpellings() {
        return altSpellings;
    }

    public String[] getTimezones() {
        return timezones;
    }

    public String[] getBorders() {
        return borders;
    }

    public String[] getCallingCodes() {
        return callingCodes;
    }

    public String[] getTopLevelDomain() {
        return topLevelDomain;
    }

    public String[] getCurrencies() {
        return currencies;
    }

    public String[] getLanguages() {
        return languages;
    }

    public Map getTranslations() {
        return translations;
    }

    public String getRelevance() {
        return relevance;
    }

    public int getPopulation() {
        return population;
    }

    public int getArea() {
        return area;
    }

    public int getGini() {
        return gini;
    }

    public int[] getLatlng() {
        return latlng;
    }


}
