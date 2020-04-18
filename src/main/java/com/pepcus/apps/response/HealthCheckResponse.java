package com.pepcus.apps.response;

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
