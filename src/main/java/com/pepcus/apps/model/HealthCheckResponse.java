package com.pepcus.apps.model;

import lombok.Data;

/**
 * Model class to hold system health check response  attributes
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Data
public class HealthCheckResponse {
  private Integer status;
  private String version;
}
