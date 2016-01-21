package com.flare576.restCountries.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * If there was more than one JSP in the project, this is where they'd be mapped. Poor index; so lonely.
 *
 * Created by Flare576 on 1/19/2016.
 */
@Controller
public class ContentController {

    @RequestMapping(value="/")
    public String index(){
        return "index";
    }
}
