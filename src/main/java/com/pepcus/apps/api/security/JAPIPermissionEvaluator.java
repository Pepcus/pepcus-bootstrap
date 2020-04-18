package com.pepcus.apps.api.security;

import java.io.Serializable;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Class used for evaluating permissions on the basis of Authentication object
 * 
 */
@Service
public class JAPIPermissionEvaluator implements PermissionEvaluator {
    
    //@Autowired
    ResourceAccessEvaluatorFactory accessEvaluatorFactory;

    
    /**
     * To evaluate permission for user and provide access of requested API
     * 
     * @param authentication
     * @param targetId
     * @param resource  
     * @param permission
     * @return
     */
    private boolean evaluatePermissions(Authentication authentication,
            Serializable targetId, String resource, String permission) {

        ResourceAccessEvaluator resourceAccessEvaluator = null;
        try {
            resourceAccessEvaluator = accessEvaluatorFactory.getService(resource);
        } catch (NoSuchBeanDefinitionException e) {
        	return true;
        }

        return resourceAccessEvaluator
                .evaluate(authentication, resource, permission, targetId);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object resource,
            Object permission) {
        return evaluatePermissions(authentication, null, (String) resource,
                (String) permission);

    }

    @Override
    public boolean hasPermission(Authentication authentication,
            Serializable targetId, String resource, Object permission) {
        return evaluatePermissions(authentication, targetId, resource,
                (String) permission);
    }
    
}