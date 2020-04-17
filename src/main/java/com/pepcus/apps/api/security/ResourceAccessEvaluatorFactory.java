package com.pepcus.apps.api.security;

/**
 * Factory class to generate objects wrt service name
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
