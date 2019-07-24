package com.linjingc.ssologindemo.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Service
@FeignClient("OAUTHSERVER")
public interface OauthService {

    @PostMapping("/oauth/token")
    public Map<String, Object> postAccessToken(@RequestParam Map<String, String> parameters);
}
