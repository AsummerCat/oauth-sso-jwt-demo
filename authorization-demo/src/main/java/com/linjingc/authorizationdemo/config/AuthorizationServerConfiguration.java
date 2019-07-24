package com.linjingc.authorizationdemo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

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
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;


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
                .accessTokenValiditySeconds(3600)
                .redirectUris("http://localhost:8600/login")
                .autoApprove(true);
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
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //访问tokenkey时需要经过认证
        security.tokenKeyAccess("isAuthenticated()");
        //security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
             // .allowFormAuthenticationForClients(); //允许表单认证  这段代码在授权码模式下会导致无法根据code　获取token　

    }


    /**
     * TokenStore
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 生成JTW token
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("linjingc");
        return converter;
    }

}
