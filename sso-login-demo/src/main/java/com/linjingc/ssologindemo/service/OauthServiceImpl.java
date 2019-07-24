package com.linjingc.ssologindemo.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class OauthServiceImpl implements OauthService {
    @Resource
    private OauthService oauthService;
    @Override
    public Map<String, Object> postAccessToken(Map<String, String> parameters) {
        return oauthService.postAccessToken(parameters);
    }
}
