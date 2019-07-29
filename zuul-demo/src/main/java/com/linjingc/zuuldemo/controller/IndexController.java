package com.linjingc.zuuldemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cxc
 * 进入首页方法
 */
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


    /**
     * 解析jwtToken
     *
     * @param user
     * @return
     */
    @GetMapping("/jwtUser")
    @ResponseBody
    public Authentication jwtUser(Authentication user) {
        System.out.println("获取到用户" + user.getName());
        Object details = user.getDetails();

        String accessToken = ((OAuth2AuthenticationDetails) details).getTokenValue();
        System.out.println("jwt: " + JwtHelper.decode(accessToken));
        return user;
    }
}
