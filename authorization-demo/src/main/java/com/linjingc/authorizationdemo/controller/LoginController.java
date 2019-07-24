package com.linjingc.authorizationdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录
 */
@Controller
public class LoginController {

    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "进入认证服务器";
    }

    @GetMapping("/authentication/require")
    public String login(){
        return "login";
    }
}
