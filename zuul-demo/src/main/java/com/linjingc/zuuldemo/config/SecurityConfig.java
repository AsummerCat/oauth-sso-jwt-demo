package com.linjingc.zuuldemo.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.csrf().disable();
        http.
                authorizeRequests()
                // 必须登录过的用户才可以进行 oauth2 的授权码申请
                //不需要权限访问
                .antMatchers("/**.html", "/**.html", "/**.css", "/img/**", "/**.js", "/ssoLoginServer/login", "/oauthServer/*", "/").permitAll()
                .anyRequest().authenticated();
    }
}