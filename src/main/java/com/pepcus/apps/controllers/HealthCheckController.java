package com.pepcus.apps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pepcus.apps.exception.ApplicationException;
import com.pepcus.apps.model.HealthCheckResponse;
import com.pepcus.apps.services.HealthCheckService;



/**
 * HealthCheck Controller for performing operations related System Health.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@RestController
@RequestMapping(path = "/v2/healthcheck")
public class HealthCheckController {

  @Autowired
  HealthCheckService healthCheckService;

  @Autowired
  private ApplicationContext appContext;

  /**
   * Get service to check system status (up or down)
   *
   * @return HealthCheckResponse object
   * @throws ApplicationException
   *
   */
  @GetMapping()
  public ResponseEntity<HealthCheckResponse> checkApplicationStatus() throws ApplicationException {
    healthCheckService.setAppContext(appContext);
    HealthCheckResponse hr = healthCheckService.getHeartBeat();
    return new ResponseEntity<HealthCheckResponse>(hr, HttpStatus.OK);
  }

}
