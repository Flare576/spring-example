package com.flare576.restCountries.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Flare576 on 1/19/2016.
 */
@Controller
public class ContentController {

    @RequestMapping(value="/")
    public String index(){
        return "index";
    }
}
