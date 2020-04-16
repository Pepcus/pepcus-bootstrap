package com.pepcus.product.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.pepcus.product.api.interceptor.RequestAttributeInterceptor;

/**
 * 
 * Application Configuration File
 */
@Configuration
@EnableWebMvc
@EnableAsync
public class AppConfig implements WebMvcConfigurer   {
  
  /**
   * Added Interceptor as Bean
   * @return
   */
  @Bean
  public RequestAttributeInterceptor requestAttributeInterceptor(){
    return new RequestAttributeInterceptor();
  }
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(requestAttributeInterceptor());
  }

}
