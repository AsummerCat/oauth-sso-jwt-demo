package com.linjingc.zuuldemo.fallbackProvider.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class contr {
    @RequestMapping("/")
    public String index(){
        return "进入首页";
    }
}
