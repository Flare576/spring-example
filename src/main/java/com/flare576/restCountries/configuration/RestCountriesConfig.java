package com.flare576.restCountries.configuration;

import com.flare576.restCountries.io.RestCountriesProxy;
import com.flare576.restCountries.service.QueryService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Flare576 on 1/18/2016.
 */
@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
@ComponentScan("com.flare576.restCountries")
public class RestCountriesConfig {
    @Autowired
    private Environment env;

    @Bean
    public CloseableHttpClient httpClient(){
        return HttpClients.createDefault();
    }

    @Bean
    public RestCountriesProxy restCountriesProxy(){
        return new RestCountriesProxy(httpClient());
    }

    @Bean
    public QueryService queryService(){
        return new QueryService(restCountriesProxy());
    }


    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
