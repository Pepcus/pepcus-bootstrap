package com.pepcus.apps.security.permission;

/**
 * Factory class to generate objects write service name
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public interface ResourceAccessEvaluatorFactory {

  /**
   * To return instance of ResourceAccessEvaluator for given serviceName
   * 
   * @param serviceName
   * @return
   */
  public ResourceAccessEvaluator getService(String serviceName);

}
