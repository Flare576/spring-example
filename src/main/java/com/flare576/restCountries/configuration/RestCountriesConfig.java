package com.flare576.restCountries.configuration;

import com.flare576.restCountries.io.RestCountriesProxy;
import com.flare576.restCountries.service.QueryService;
import com.flare576.restCountries.service.QueryServiceImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Project configuration. Sets up resource handlers, view resolvers, and Beans for the project.
 *
 * Created by Flare576 on 1/18/2016.
 */
@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
@ComponentScan("com.flare576.restCountries")
public class RestCountriesConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");
        registry.addResourceHandler("/html/**").addResourceLocations("/WEB-INF/html/");
    }

    @Bean
    public ViewResolver getViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

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
        return new QueryServiceImpl(restCountriesProxy());
    }


    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
