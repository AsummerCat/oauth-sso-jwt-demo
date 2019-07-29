package com.linjingc.zuuldemo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @deprecated 自定义oauth2 注销方法
 */

@Component
@Qualifier("mySsoLogoutHandler")
public class MySsoLogoutHandler extends SecurityContextLogoutHandler implements LogoutHandler {

    @Value("${auth-server}/${auth-server-logout-method}")
    String logoutUrl;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        Object details = authentication.getDetails();
        if (details.getClass().isAssignableFrom(OAuth2AuthenticationDetails.class)) {

            String accessToken = ((OAuth2AuthenticationDetails) details).getTokenValue();

            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("access_token", accessToken);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "bearer " + accessToken);

            HttpEntity<String> request = new HttpEntity(params, headers);

            HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();
            restTemplate.setMessageConverters(Arrays.asList(new HttpMessageConverter[]{formHttpMessageConverter, stringHttpMessageConverternew}));
            try {
                ResponseEntity<String> response = restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class);
            } catch (HttpClientErrorException e) {
                //LOGGER.error("HttpClientErrorException invalidating token with SSO authorization server. response.status code: {}, server URL: {}", e.getStatusCode(), logoutUrl);
            }

        }
    }
}