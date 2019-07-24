package com.linjingc.authorizationdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthorizationDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationDemoApplication.class, args);
    }

}
