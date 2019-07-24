package com.linjingc.authorizationdemo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
    RedisTemplate redisTemplate;


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
     *
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
        String salt = BCrypt.gensalt(); // 实时生成加密的salt
        // redisTemplate.opsForValue().set("token:" + user.getUsername(), salt, 3600, TimeUnit.SECONDS);
        converter.setSigner(new MacSigner(salt));
        return converter;
    }


    //@Bean
    //public TokenEnhancer tokenEnhancer() {
    //    return new CustomTokenEnhancer();
    //}
    //
    //public class CustomTokenEnhancer implements TokenEnhancer {
    //    @Override
    //    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    //        Map<String, Object> additionalInfo = new HashMap<>();
    //        additionalInfo.put("organization", authentication.getName());
    //        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    //        return accessToken;
    //    }
//}

/**
 * String salt = BCrypt.gensalt(); // 实时生成加密的salt
 * redisTemplate.opsForValue().set("token:"+user.getUsername(), salt, 3600, TimeUnit.SECONDS);
 * <p>
 * Algorithm algorithm = Algorithm.HMAC256(salt);
 * //Date date = new Date(System.currentTimeMillis()+3600*1000);  //设置1小时后过期
 * <p>
 * return JWT.create()
 * .withSubject(user.getUsername())
 * .withExpiresAt(DateUtils.addDays(new Date(), 1))
 * .withIssuedAt(new Date())
 * .sign(algorithm);
 */
}
