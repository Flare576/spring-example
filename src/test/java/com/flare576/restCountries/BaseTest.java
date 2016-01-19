package com.flare576.restCountries;

import org.junit.BeforeClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Flare576 on 1/18/2016.
 */
public class BaseTest {
    public static Properties properties;

    public static void setupClass() throws IOException {
        properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("application.properties"));
    }


    /**
     * Some sample JSON objects for the response
     */

    public static final String MADAGASCAR_JSON = "{\n" +
            "        \"name\": \"Madagascar\",\n" +
            "        \"capital\": \"Antananarivo\",\n" +
            "        \"altSpellings\": [\n" +
            "            \"MG\",\n" +
            "            \"Republic of Madagascar\",\n" +
            "            \"Repoblikan'i Madagasikara\",\n" +
            "            \"République de Madagascar\"\n" +
            "        ],\n" +
            "        \"relevance\": \"0\",\n" +
            "        \"region\": \"Africa\",\n" +
            "        \"subregion\": \"Eastern Africa\",\n" +
            "        \"translations\": {\n" +
            "            \"de\": \"Madagaskar\",\n" +
            "            \"es\": \"Madagascar\",\n" +
            "            \"fr\": \"Madagascar\",\n" +
            "            \"ja\": \"マダガスカル\",\n" +
            "            \"it\": \"Madagascar\"\n" +
            "        },\n" +
            "        \"population\": 22434363,\n" +
            "        \"latlng\": [\n" +
            "            -20,\n" +
            "            47\n" +
            "        ],\n" +
            "        \"demonym\": \"Malagasy\",\n" +
            "        \"area\": 587041,\n" +
            "        \"gini\": 44.1,\n" +
            "        \"timezones\": [\n" +
            "            \"UTC+03:00\"\n" +
            "        ],\n" +
            "        \"borders\": [],\n" +
            "        \"nativeName\": \"Madagasikara\",\n" +
            "        \"callingCodes\": [\n" +
            "            \"261\"\n" +
            "        ],\n" +
            "        \"topLevelDomain\": [\n" +
            "            \".mg\"\n" +
            "        ],\n" +
            "        \"alpha2Code\": \"MG\",\n" +
            "        \"alpha3Code\": \"MDG\",\n" +
            "        \"currencies\": [\n" +
            "            \"MGA\"\n" +
            "        ],\n" +
            "        \"languages\": [\n" +
            "            \"fr\",\n" +
            "            \"mg\"\n" +
            "        ]\n" +
            "    }";
    public static final String UNITEDSTATES_JSON = "{\n" +
            "        \"name\": \"United States\",\n" +
            "        \"capital\": \"Washington D.C.\",\n" +
            "        \"altSpellings\": [\n" +
            "            \"US\",\n" +
            "            \"USA\",\n" +
            "            \"United States of America\"\n" +
            "        ],\n" +
            "        \"relevance\": \"3.5\",\n" +
            "        \"region\": \"Americas\",\n" +
            "        \"subregion\": \"Northern America\",\n" +
            "        \"translations\": {\n" +
            "            \"de\": \"Vereinigte Staaten von Amerika\",\n" +
            "            \"es\": \"Estados Unidos\",\n" +
            "            \"fr\": \"États-Unis\",\n" +
            "            \"ja\": \"アメリカ合衆国\",\n" +
            "            \"it\": \"Stati Uniti D'America\"\n" +
            "        },\n" +
            "        \"population\": 321645000,\n" +
            "        \"latlng\": [\n" +
            "            38,\n" +
            "            -97\n" +
            "        ],\n" +
            "        \"demonym\": \"American\",\n" +
            "        \"area\": 9629091,\n" +
            "        \"gini\": 48,\n" +
            "        \"timezones\": [\n" +
            "            \"UTC−12:00\",\n" +
            "            \"UTC−11:00\",\n" +
            "            \"UTC−10:00\",\n" +
            "            \"UTC−09:00\",\n" +
            "            \"UTC−08:00\",\n" +
            "            \"UTC−07:00\",\n" +
            "            \"UTC−06:00\",\n" +
            "            \"UTC−05:00\",\n" +
            "            \"UTC−04:00\",\n" +
            "            \"UTC+10:00\",\n" +
            "            \"UTC+12:00\"\n" +
            "        ],\n" +
            "        \"borders\": [\n" +
            "            \"CAN\",\n" +
            "            \"MEX\"\n" +
            "        ],\n" +
            "        \"nativeName\": \"United States\",\n" +
            "        \"callingCodes\": [\n" +
            "            \"1\"\n" +
            "        ],\n" +
            "        \"topLevelDomain\": [\n" +
            "            \".us\"\n" +
            "        ],\n" +
            "        \"alpha2Code\": \"US\",\n" +
            "        \"alpha3Code\": \"USA\",\n" +
            "        \"currencies\": [\n" +
            "            \"USD\",\n" +
            "            \"USN\",\n" +
            "            \"USS\"\n" +
            "        ],\n" +
            "        \"languages\": [\n" +
            "            \"en\"\n" +
            "        ]\n" +
            "    }";
}
