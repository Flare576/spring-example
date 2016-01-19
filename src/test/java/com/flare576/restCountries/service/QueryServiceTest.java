package com.flare576.restCountries.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flare576.restCountries.BaseTest;
import com.flare576.restCountries.io.Country;
import com.flare576.restCountries.io.RestCountriesProxy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.spy;

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
        queryService = new QueryService(restCountriesProxy);
        when( restCountriesProxy.getCountries()).thenReturn(countries);
        when( restCountriesProxy.getLastUpdate()).thenReturn(new Date());
    }

    @Test
    public void getByBorderCount_happy(){
        Country[] result = queryService.getByBorderCount(0);
        assertEquals(result.length, 1);

        result = queryService.getByBorderCount(1);
        assertEquals(result.length, 0);

        verify(restCountriesProxy,times(1)).getLastUpdate();
    }

    @Test
    public void getByMaxBorderCount_happy(){
        Country[] result = queryService.getByMaxBorderCount();
        assertEquals(result.length, 1);
    }

    @Test
    public void getMaxBorderCount_happy(){
        Integer result = queryService.getMaxBorderCount();
        assertEquals(result, new Integer(2));
    }
}
