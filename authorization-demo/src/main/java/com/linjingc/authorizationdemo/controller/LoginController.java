package com.linjingc.authorizationdemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录
 * @author cxc
 */
@Controller
public class LoginController {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "进入认证服务器";
    }

    /**
     * 登录页
     */
    @GetMapping("/authentication/require")
    public String login() {
        return "login";
    }

    /**
     * 退出操作
     */
    @RequestMapping("oauth/exit")
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        // token can be revoked here if needed
        new SecurityContextLogoutHandler().logout(request, null, null);
        try {
            //sending back to client app
            System.out.println("退出授权服务器");
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取当前用户
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user")
    public Authentication user(Authentication user) {
        return user;
    }
}
