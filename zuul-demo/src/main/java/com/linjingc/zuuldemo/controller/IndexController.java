package com.linjingc.zuuldemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "进入首页";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/user")
    @ResponseBody
    public Authentication user(Authentication user) {
        return user;
    }

    @GetMapping("/info")
    @ResponseBody
    public void user(HttpServletRequest request) {
        request.getSession().getAttributeNames();
        request.getHeaderNames();
        request.getCookies();
    }

}
