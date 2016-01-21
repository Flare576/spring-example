package com.flare576.restCountries.io;

import com.flare576.restCountries.BaseTest;
import com.flare576.restCountries.model.Country;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

/**
 * Created by Flare576 on 1/18/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { RestCountriesProxy.class})
public class RestCountriesProxyTest extends BaseTest {
    @Mock
    CloseableHttpClient httpClient;

    private static String cacheMinutes;
    private static String restCountriesUrl;

    RestCountriesProxy restCountriesProxy;

    @BeforeClass
    public static void setupClass() throws IOException {
        BaseTest.setupClass();
        cacheMinutes = properties.getProperty("proxy.cache.minutes", "5");
        restCountriesUrl = properties.getProperty("{proxy.external.url", "http://restcountries.eu/rest");
    }
    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        restCountriesProxy = spy(new RestCountriesProxy(httpClient));
        ReflectionTestUtils.setField(restCountriesProxy, "cacheMinutes", cacheMinutes);
        ReflectionTestUtils.setField(restCountriesProxy, "restCountriesUrl", restCountriesUrl);
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        String output = "[" + UNITEDSTATES_JSON + "," + MADAGASCAR_JSON + "]";
        when( mockResponse.getEntity()).thenReturn(new StringEntity(output));
        when( httpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    }

    @Test
    public void noCache_happy() throws Exception {
        Map<String,Country> countries = restCountriesProxy.getCountries();
        assertEquals(2, countries.size());
        verifyPrivate(restCountriesProxy,times(1)).invoke("refresh");
    }


    @Test
    public void cacheExpired_happy() throws Exception {
        ReflectionTestUtils.setField(restCountriesProxy, "cacheMinutes", "0");
        doCallRealMethod().when(restCountriesProxy,"refresh");
        Map<String,Country> countries = restCountriesProxy.getCountries();
        assertEquals(2, countries.size());
        countries = restCountriesProxy.getCountries();
        assertEquals(2, countries.size());
        verifyPrivate(restCountriesProxy,times(2)).invoke("refresh");
    }

    @Test
    public void cacheGood_happy() throws Exception {
        Map<String,Country> countries = restCountriesProxy.getCountries();
        assertEquals(2, countries.size());
        countries = restCountriesProxy.getCountries();
        assertEquals(2, countries.size());
        verifyPrivate(restCountriesProxy,times(1)).invoke("refresh");
    }

}
