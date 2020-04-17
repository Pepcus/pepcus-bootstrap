package com.pepcus.apps.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import com.pepcus.apps.api.constant.ApplicationConstants;
import com.pepcus.apps.api.security.ResourceAccessEvaluatorFactory;
import com.pepcus.apps.api.security.oauth2.AppTokenEnhancer;
import com.pepcus.apps.api.services.crypto.AESEncryptorDecryptor;
import com.pepcus.apps.api.services.crypto.AppEncryptorDecryptor;
import com.pepcus.apps.api.services.crypto.BCryptPasswordEncryptor;
import com.pepcus.apps.api.services.crypto.BlowfishEncryptorDecryptor;

/**
 * Main class for Spring Boot based API application.
 * 
 */

@SpringBootApplication
public class ApiApplication {

  @Value("${com.pepcus.apps.api.crypto.algo}")
  private String cryptoAlgo;

  @Value("${com.pepcus.apps.api.crypto.encrypt.key}")
  private String key;

  @Value("${com.pepcus.apps.api.crypto.initVector}")
  private String initVector;

  @Value("${JWT.jwt_key}")
  private String jwtSigningKey;


  /**
   * Main method for spring application
   * 
   * @param args command line arguments passed to app
   * 
   */
  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  public AppTokenEnhancer accessTokenConverter() {
    AppTokenEnhancer converter = new AppTokenEnhancer();
    converter.setSigningKey(jwtSigningKey);
    return converter;
  }

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
    if (ApplicationConstants.BLOWFISH_ALGO.equalsIgnoreCase(cryptoAlgo)) {
      return new BlowfishEncryptorDecryptor(key, initVector);
    } else if (ApplicationConstants.BCRYPT_ALGO.equalsIgnoreCase(cryptoAlgo)) {
      return new BCryptPasswordEncryptor();
    }
    return new AESEncryptorDecryptor(key, initVector);

  }

}
