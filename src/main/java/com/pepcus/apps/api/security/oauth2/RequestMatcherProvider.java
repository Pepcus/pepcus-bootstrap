package com.pepcus.apps.api.security.oauth2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * Purpose of this class to provide AntRequestMatchers for url patterns those should be ignored by 
 * Authorization
 * 
 */
@Component
public class RequestMatcherProvider {

    @Value("#{'${japi.skip.authorization.urls}'.split(',')}")
    private List<String> skipAuthorizationUrls; //These urls are from configurations
    
    public static List<String> oAuthUrls;
    static { // These are static urls, so keep it seperate
        oAuthUrls = new ArrayList<String>(Arrays.asList("/v1/login",
                                                        "/v1/logout", 
                                                        "/v1/oauth/authorize",
                                                        "/v1/oauth/confirm_access",
                                                        "/v1/oauth/error",
                                                        "/v1/oauth/decode_token"));
    }
    
    /**
     * Get list of request matchers to be ignored by resource server from authorization configurations.
     * @return
     */
    public List<RequestMatcher> getResourceRequestMatchersToIgnoreAuth() {
        
        List<RequestMatcher> requestMatchers = new ArrayList<RequestMatcher>();
        
        oAuthUrls.forEach(pattern -> requestMatchers.add(new AntPathRequestMatcher(pattern)));
        skipAuthorizationUrls.forEach(pattern -> requestMatchers.add(new AntPathRequestMatcher(pattern)));
        
        return requestMatchers;
    }
    
    /**
     * Get list of request matchers.
     * @return
     */
    public List<RequestMatcher> getOAuthRequestMatchers() {
        
        List<RequestMatcher> requestMatchers = new ArrayList<RequestMatcher>();
        oAuthUrls.forEach(pattern -> requestMatchers.add(new AntPathRequestMatcher(pattern)));
        return requestMatchers;
    }
    
}
