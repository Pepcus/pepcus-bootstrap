package com.pepcus.apps.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.services.s3.AmazonS3;

/**
 * Class to configure Amazon S3 client.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Configuration
public class S3Config {

  @Value("${aws.access.key}")
  private String awsAccessKey;

  @Value("${aws.secret.key}")
  private String awsSecretKey;

  @Value("${aws.region}")
  private String awsRegion;

  @Bean
  public AmazonS3 s3client() {
    // BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    // AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(awsRegion))
    // .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
    // return s3Client;
    return null;
  }


}
