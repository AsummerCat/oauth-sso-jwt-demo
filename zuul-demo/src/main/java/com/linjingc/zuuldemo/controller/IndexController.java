package com.linjingc.zuuldemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @RequestMapping("/")
    public String index() {
        return "进入首页";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
