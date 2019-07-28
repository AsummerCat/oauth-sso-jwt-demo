package com.linjingc.zuuldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author cxc
 */
@SpringCloudApplication
@EnableZuulProxy
public class ZuulDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulDemoApplication.class, args);
    }

    //protected void configure(HttpSecurity http) throws Exception {
    //    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    //    http.csrf().disable(); //关闭csrf
    //    http.authorizeRequests().anyRequest().authenticated().and().httpBasic(); //开启认证
    //}
}
