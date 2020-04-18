package com.pepcus.apps.security.oauth2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 * This class is used to configure and authenticate resource owners 
 * by authentication manager and BCrypt Password encoder for OAuth2 
 * /auth and /token end points.
 * 
 */
@Configuration 
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private AppsUserDetailsService thrUserDetailsService;
    
    @Autowired
    private RequestMatcherProvider requestMatcherProvider;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(thrUserDetailsService).passwordEncoder(encoder());
    }
    
    @Bean
    public PasswordEncoder encoder() {
        return new AppBCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
        .requestMatchers(new OrRequestMatcher(requestMatcherProvider.
                getOAuthRequestMatchers()))
        .and()
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/v1/login").permitAll()
        .and()
        .logout().permitAll();
    }
    
}