package com.linjingc.resourcedemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @author MrBird
 */
@RestController
public class UserController {

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/user")
    public Principal user(Principal user) {
        System.out.println("获取到用户" + user.getName());
        return user;
    }


    @GetMapping("auth/test1")
    //@PreAuthorize("hasAuthority('user:add')")
    public String authTest1() {
        return "您拥有'user:add'权限";
    }

    @GetMapping("auth/test2")
    @PreAuthorize("hasAuthority('user:update')")
    public String authTest2() {
        return "您拥有'user:update'权限";
    }
}
