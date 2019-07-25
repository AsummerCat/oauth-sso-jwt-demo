package com.linjingc.ssologindemo.controller;

import com.linjingc.ssologindemo.service.OauthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MrBird
 */
@RestController
public class UserController {
    @Autowired
    private OauthServiceImpl oauthService;

    @GetMapping("/info")
    public String info() {
        return "info";
    }

    @GetMapping("/user")
    public Authentication user(Authentication user) {
        return user;
    }

}
