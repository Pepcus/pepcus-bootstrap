package com.pepcus.apps.security.oauth2.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.pepcus.apps.services.AppClientDetailsService;

/**
 * Class to enable authorization server by OAuth2 
 * for generating access_token and refresh_token with 
 * the help of various grant types. 
 * 
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerSecurityConfig extends AuthorizationServerConfigurerAdapter {
	
    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private AppTokenEnhancer tokenEnhancer;
    
    @Autowired
	private AppsUserDetailsService thrUserDetailsService;
    
    @Autowired
    private AppClientDetailsService clientDetailsService;
    
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.withClientDetails(clientDetailsService);  
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer));
        endpoints.tokenStore(tokenStore)
                 .tokenEnhancer(enhancerChain) 
                 .authenticationManager(authenticationManager)
                 .userDetailsService(thrUserDetailsService);
        
        //To override Authorization endpoint path mappings with prefix /v1
        endpoints.pathMapping("/oauth/authorize", "/v1/oauth/authorize")
        .pathMapping("/oauth/token", "/v1/oauth/token")
        .pathMapping("/oauth/confirm_access", "/v1/oauth/confirm_access")
        .pathMapping("/oauth/error", "/v1/oauth/error")
        .pathMapping("/oauth/check_token", "/v1/oauth/decode_token");
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("permitAll()");
    }
}