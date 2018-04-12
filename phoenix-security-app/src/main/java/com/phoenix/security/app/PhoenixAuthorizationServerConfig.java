package com.phoenix.security.app;

import com.phoenix.security.core.properties.OAuth2ClientProperties;
import com.phoenix.security.core.properties.SecurityProperties;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sheng
 * Date: 2018-04-06 15:17
 * Description:
 */
@Configuration
@EnableAuthorizationServer
public class PhoenixAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //认证时需要的AuthenticationManager
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    /**
     * 配置入口点TokenEndPoints的
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        if(jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {

            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
            //************注意：jwtTokenEnhancer一定要在jwtAccessTokenConverter之前添加，否则在发出的token中无法获取jwtTokenEnhancer中自定义的信息（如company）
            tokenEnhancers.add(jwtTokenEnhancer);
            tokenEnhancers.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

            System.out.println(jwtTokenEnhancer);

            endpoints.tokenEnhancer(tokenEnhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }

    }

    /**
     * 设置clients，在这儿可以设置clientId和clientSecret，而且在这儿设置了之后，在application.properties中设置的就无效了
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        OAuth2ClientProperties[] clientProperties = securityProperties.getOauth2().getClients();

        if(ArrayUtils.isNotEmpty(clientProperties)) {
            for(OAuth2ClientProperties config : clientProperties) {
                builder.withClient(config.getClientId()) //设置clientId
                        .secret(config.getClientSecret())      //设置clientSecret
                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())  //设置token信息的有效期
                        .authorizedGrantTypes("refresh_token","password")           //设置针对该client的授权模式
                        .scopes("all","read","write");//
            }
        }

        /*clients.inMemory()  //设置client信息存在内存中还是数据库中
                .withClient("phoenix") //设置clientId
                .secret("phoenixsecret")      //设置clientSecret
                .accessTokenValiditySeconds(7200)  //设置token信息的有效期
                .authorizedGrantTypes("refresh_token","password")           //设置针对该client的授权模式
                .scopes("all","read","write");      //

                //.and().clients("")  //还可以设置为更多的client授权*/
    }
}
