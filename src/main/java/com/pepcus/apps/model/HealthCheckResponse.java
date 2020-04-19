package com.pepcus.apps.model;

import lombok.Data;

/**
 * HealthCheck Data
 *
 */
@Data
public class HealthCheckResponse {
    private Integer status;
    private String version;
}
