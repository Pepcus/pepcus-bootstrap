package com.pepcus.apps.api.response;

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
