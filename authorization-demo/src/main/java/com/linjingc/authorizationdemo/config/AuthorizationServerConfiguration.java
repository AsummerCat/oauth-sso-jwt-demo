package com.linjingc.authorizationdemo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权服务
 *
 * @author cxc
 * @date 2019年7月11日16:38:56
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 声明单个客户端及其属性 最少一个 不然无法启动
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //这个地方后面会使用到 前缀表示密码加密类型
        String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");

        clients.inMemory()
                .withClient("client")
                .secret(finalSecret)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .scopes("all")
                .accessTokenValiditySeconds(60)
                .redirectUris("http://my.cloud.com/login")
                .autoApprove(true)
                .refreshTokenValiditySeconds(200);
    }

    /**
     * 配置授权服务器终结点的非安全功能，如令牌存储、令牌自定义、用户批准和授予类型 请求方式
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore()).accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                //允许 GET、POST 请求获取 token，即访问端点：oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                //刷新token
                .reuseRefreshTokens(true);
    }

    /**
     * 配置（授权服务器安全配置器安全）
     * 访问tokenkey时需要经过认证
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //访问tokenkey时需要经过认证
        //security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()")//公开/oauth/token的接口
        //        .checkTokenAccess("permitAll()")
        security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()")
        .allowFormAuthenticationForClients();
    }


    /**
     * TokenStore
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 生成JTW token
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("linjingc");
        return converter;
    }


    /**
     * 用于扩展JWT
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
    public TokenEnhancer jwtTokenEnhancer() {
        return new MyJwtTokenEnhancer();
    }

    /**
     * 扩展token内容
     */
    public class MyJwtTokenEnhancer implements TokenEnhancer {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            final Map<String, Object> additionalInfo = new HashMap<>();
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("username", user.getUsername());
            additionalInfo.put("authorities_", user.getAuthorities());

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        }
    }
}
