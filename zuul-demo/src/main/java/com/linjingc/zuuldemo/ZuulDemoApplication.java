package com.linjingc.zuuldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * @author cxc
 */
@SpringCloudApplication
@EnableZuulProxy
public class ZuulDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulDemoApplication.class, args);
    }
}
