package com.linjingc.ssologindemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
//@EnableOAuth2Sso
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(3)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();

//        http.antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/login**")
//                .permitAll()
//                .anyRequest()
//                .authenticated();
    }
}