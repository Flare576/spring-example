package com.flare576.restCountries.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flare576.restCountries.BaseTest;
import com.flare576.restCountries.io.RestCountriesProxy;
import com.flare576.restCountries.model.Country;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Flare576 on 1/18/2016.
 */
public class QueryServiceTest extends BaseTest {

    @Mock
    RestCountriesProxy restCountriesProxy;

    private static ObjectMapper objectMapper = new ObjectMapper();
    private QueryService queryService;
    private static Map<String,Country> countries;

    @BeforeClass
    public static void classSetup() throws IOException {
        countries = new HashMap<>();
        addCountry(UNITEDSTATES_JSON);
        addCountry(MADAGASCAR_JSON);
    }

    private static void addCountry(String json) throws IOException {
        Country country = objectMapper.readValue(json, Country.class);
        countries.put(country.getAlpha3Code(),country);
    }

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        queryService = new QueryServiceImpl(restCountriesProxy);
        when( restCountriesProxy.getCountries()).thenReturn(countries);
        when( restCountriesProxy.getLastUpdate()).thenReturn(new Date());
    }

    @Test
    public void testGetByBorderCount_happy(){
        Country[] result = queryService.getByBorderCount(0);
        assertEquals(1, result.length);

        result = queryService.getByBorderCount(1);
        assertEquals(0, result.length);

        verify(restCountriesProxy,times(1)).getLastUpdate();
    }

    @Test
    public void testGetByMaxBorderCount_happy(){
        Country[] result = queryService.getByMaxBorderCount();
        assertEquals(1, result.length);
    }

    @Test
    public void testGetMaxBorderCount_happy(){
        Integer result = queryService.getMaxBorderCount();
        assertEquals(new Integer(2), result);
    }

    @Test
    public void testGetByTimezoneCount_happy(){
        Country[] result = queryService.getByTimezoneCount(11);
        assertEquals(1, result.length);

        result = queryService.getByTimezoneCount(0);
        assertEquals(0, result.length);

        verify(restCountriesProxy,times(1)).getLastUpdate();
    }

    @Test
    public void testGetByMaxTimezoneCount_happy(){
        Country[] result = queryService.getByMaxTimezoneCount();
        assertEquals(1, result.length);
    }

    @Test
    public void testGetMaxTimezoneCount_happy(){
        Integer result = queryService.getMaxTimezoneCount();
        assertEquals(new Integer(11), result);
    }

    @Test
    public void testGetCountriesByAlpha3_oneparam_happy(){
        Country[] result = queryService.getCountriesByAlpha3(USA_ALPHA3);
        assertEquals(1, result.length);
    }
    @Test
    public void testGetCountriesByAlpha3_twoparam_happy(){
        Country[] result = queryService.getCountriesByAlpha3(USA_ALPHA3+","+MADAGASCAR_ALPHA3);
        assertEquals(2, result.length);
    }
    @Test
    public void testGetCountriesByAlpha3_bad_param(){
        Country[] result = queryService.getCountriesByAlpha3(INVALID_ALPHA3);
        assertEquals(0, result.length);
    }
}
