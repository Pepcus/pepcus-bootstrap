package com.pepcus.apps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main class for Spring Boot based API application.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@SpringBootApplication
public class ApiApplication {

  private static Logger logger = LoggerFactory.getLogger(ApiApplication.class);

  /**
   * Main method for spring application
   * 
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
    logger.info("##### PEPCUS - BOOTSTRAP #####");
  }

}
