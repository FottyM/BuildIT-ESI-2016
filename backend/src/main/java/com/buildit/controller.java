package com.buildit;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rain on 29.03.16.
 */

@Controller
public class controller {

    @RequestMapping(value = "/test")
    public String name(){
        return "test";
    }

}
