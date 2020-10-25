package com.luca.single_node_pro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author luca
 * @create 2020-09-24 08:20
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String index(){

        return "index";
    }
    @RequestMapping("index")
    public String index1(){

        return "index";
    }

}
