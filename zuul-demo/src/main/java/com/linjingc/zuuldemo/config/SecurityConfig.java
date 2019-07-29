package com.linjingc.zuuldemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MySsoLogoutHandler mySsoLogoutHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                //不需要权限访问
                .antMatchers("/**.html", "/**.html", "/**.css", "/img/**", "/**.js", "/").permitAll()
                .anyRequest().authenticated().
                // this LogoutHandler invalidate user token from SSO
                        and().logout().logoutSuccessUrl("http://my.oauth.com:8200/oauth/exit")
                .deleteCookies("JSESSIONID", "ANY_OTHER_COOKIE").permitAll().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }
}