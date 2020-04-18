package com.pepcus.apps.security.permission;

import static com.pepcus.apps.constant.ApplicationConstants.API_ACCESS_TOKEN;
import static com.pepcus.apps.constant.ApplicationConstants.APP_AUTH_DATA;
import static com.pepcus.apps.constant.ApplicationConstants.ROLE_PARAM;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.pepcus.apps.model.AppAuthData;
import com.pepcus.apps.security.oauth2.services.AppTokenEnhancer;

/**
 * Filter used to collect data from authorization token required for application.
 * for role based access control
 * 
 */

public class AppAuthDataCollectFilter extends BasicAuthenticationFilter {

    private HandlerExceptionResolver resolver;
    
    private AppTokenEnhancer tokenEnhancer; 
    
    public AppAuthDataCollectFilter(
            AuthenticationManager authenticationManager, AppTokenEnhancer tokenEnhancer,
            HandlerExceptionResolver resolver) {
        super(authenticationManager);
        
        this.tokenEnhancer = tokenEnhancer;
        this.resolver = resolver;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            Object tokenObj = request.getAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
            if (tokenObj != null) {
                String token = (String)tokenObj;
                if (StringUtils.isNotBlank(token)) {
                    //Setting accessToken to request attribute for use in Handbook Get Content API.
                    request.setAttribute(API_ACCESS_TOKEN, token); 
                    AppAuthData appAuthData = tokenEnhancer.getAppAuthData(token);
                    //Setting data to request attributes which we can fetch in future
                    request.setAttribute(APP_AUTH_DATA, appAuthData);
                    request.setAttribute(ROLE_PARAM, appAuthData.getRole());
                    request.setAttribute("REQUEST_PARAM", request.getParameterMap());
                }
            }
        } catch (Exception ex) {
            resolver.resolveException(request, response, null, ex);
            return;
        }
        chain.doFilter(request, response); 
    }
    
}