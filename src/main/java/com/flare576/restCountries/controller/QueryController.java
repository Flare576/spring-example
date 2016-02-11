package com.flare576.restCountries.controller;

import com.flare576.restCountries.model.Country;
import com.flare576.restCountries.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * All v1 endpoints. If this project was extend, we'd probably want to split this into controllers per type:
 * /v1/border in BorderController
 * /v1/timezone in TimezoneController
 * etc.
 *
 * Created by Flare576 on 1/18/2016.
 */
@Controller
@RequestMapping("/v1")
public class QueryController {
    @Autowired
    QueryService queryService;

    @RequestMapping(value="/alpha", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Country[]> getCountriesByAlpha3(@RequestParam String codes){
        return new ResponseEntity<>(queryService.getCountriesByAlpha3(codes), HttpStatus.OK);
    }

    @RequestMapping(value="/border/{qty:[\\d]+}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Country[]> getCountriesWithBorderCount(@PathVariable Integer qty){
        return new ResponseEntity<>(queryService.getByBorderCount(qty), HttpStatus.OK);
    }

    @RequestMapping(value="/border/max", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Country[]> getCountriesWithMaxBorderCount(){
        return new ResponseEntity<>(queryService.getByMaxBorderCount(), HttpStatus.OK);
    }

    @RequestMapping(value="/border/maxQty", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getCountriesMaxBorderCount(){
        return new ResponseEntity<>(queryService.getMaxBorderCount(), HttpStatus.OK);
    }

    @RequestMapping(value="/timezone/{qty:[\\d]+}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Country[]> getCountriesWithTimezoneCount(@PathVariable Integer qty){
        return new ResponseEntity<>(queryService.getByTimezoneCount(qty), HttpStatus.OK);
    }

    @RequestMapping(value="/timezone/max", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Country[]> getCountriesWithMaxTimezoneCount(){
        return new ResponseEntity<>(queryService.getByMaxTimezoneCount(), HttpStatus.OK);
    }

    @RequestMapping(value="/timezone/maxQty", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getCountriesMaxTimezoneCount(){
        return new ResponseEntity<>(queryService.getMaxTimezoneCount(), HttpStatus.OK);
    }
}
