package com.pepcus.apps.config;


import static com.pepcus.apps.constant.ApplicationConstants.PATTERN_V1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.pepcus.apps.constant.ApplicationConstants;
import com.pepcus.apps.interceptors.APIProcessingTimeInterceptor;
import com.pepcus.apps.security.oauth2.services.AppTokenEnhancer;
import com.pepcus.apps.security.permission.ResourceAccessEvaluatorFactory;
import com.pepcus.apps.services.crypto.AESEncryptorDecryptor;
import com.pepcus.apps.services.crypto.AppEncryptorDecryptor;
import com.pepcus.apps.services.crypto.BCryptPasswordEncryptor;

/**
 * Class to load application configurations
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Configuration
@EnableWebMvc
@EnableAsync
public class AppConfig extends WebMvcConfigurerAdapter {

  // Encryption algorithm name
  @Value("${com.pepcus.apps.crypto.algo}")
  private String cryptoAlgo;

  // Encryption key
  @Value("${com.pepcus.apps.crypto.encrypt.key}")
  private String key;

  // For AES algorithm as it must be 16 bytes long
  @Value("${com.pepcus.apps.crypto.initVector}")
  private String initVector;

  // JWT token Key
  @Value("${JWT.jwt_key}")
  private String jwtSigningKey;

  /**
   * To read authentication from token store
   * 
   * @return
   */
  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * To set token information
   * 
   * @return
   */
  @Bean
  public AppTokenEnhancer accessTokenConverter() {
    AppTokenEnhancer converter = new AppTokenEnhancer();
    converter.setSigningKey(jwtSigningKey);
    return converter;
  }

  /**
   * To get service instance based on service name from service factory
   * 
   * @return
   */
  @Bean
  public ServiceLocatorFactoryBean accessEvaluatorFactory() {
    ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
    bean.setServiceLocatorInterface(ResourceAccessEvaluatorFactory.class);
    return bean;
  }

  /**
   * Facilitates messageSoruce
   * 
   * @return MessageSource
   */
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    return messageSource;
  }

  /**
   * Facilitates Encryption/Decryption technique for password field.
   * 
   * @return
   */
  @Bean
  @Lazy(value = true)
  public AppEncryptorDecryptor getEncryptor() {
    if (ApplicationConstants.BCRYPT_ALGO.equalsIgnoreCase(cryptoAlgo)) {
      return new BCryptPasswordEncryptor();
    } 
    // Default encryptor ApplicationConstants.AES_ALGO
    return new AESEncryptorDecryptor(key, initVector);

  }

  /**
   * Facilitates to register custom intercepter
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new APIProcessingTimeInterceptor()).addPathPatterns(PATTERN_V1);
  }

  /**
   * Facilitates to register custom view
   */
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/v1/login").setViewName("v1/login");
    registry.addViewController("/v1/oauth/confirm_access").setViewName("v1/confirm_access");
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }


  /**
   * Facilitates to set default content type for application
   */
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }


  /**
   * Facilitates to register resources
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/v1/static/**").addResourceLocations("classpath:/static/");
    // registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

}
