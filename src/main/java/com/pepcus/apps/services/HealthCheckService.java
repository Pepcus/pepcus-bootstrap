package com.pepcus.apps.services;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.pepcus.apps.model.HealthCheckResponse;


/**
 * Provides a collection of all services for system health check
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Service
public class HealthCheckService {
  private Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

  @Value("${application.version}")
  private String version;

  private ApplicationContext appContext;

  /**
   * Provide System Health checks
   * 
   * @return
   */
  public HealthCheckResponse getHeartBeat() {
    HealthCheckResponse hr = new HealthCheckResponse();
    hr.setVersion(version);
    try {
      // See if we have a database connection
      DataSource ds = (DataSource) appContext.getBean("dataSource");
      hr.setStatus(ds.getConnection() == null ? 0 : 1);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
      hr.setStatus(0);
    }
    return hr;
  }

  public void setAppContext(ApplicationContext appContext) {
    this.appContext = appContext;
  }
}
