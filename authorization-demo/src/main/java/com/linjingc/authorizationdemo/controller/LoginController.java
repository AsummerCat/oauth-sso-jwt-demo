package com.linjingc.authorizationdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
    @Autowired
    private TokenStore tokenStore;

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
        //首先移除认证服务器上的session
        new SecurityContextLogoutHandler().logout(request, null, null);
        try {
            String token = request.getHeader("Authorization");
            if (token != null) {
                String tokenValue = token.replace("bearer ", "").trim();
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenValue);
                if (oAuth2AccessToken != null) {
                    //移除access_token
                    tokenStore.removeAccessToken(oAuth2AccessToken);
                    //移除refresh_token
                    tokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
                }
            }
            //sending back to client app
            System.out.println("退出授权服务器");
            response.sendRedirect(request.getHeader("referer"));
        } catch (Exception e) {
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
