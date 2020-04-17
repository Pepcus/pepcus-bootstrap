package com.pepcus.apps.api.security;

import java.io.Serializable;

import org.springframework.security.core.Authentication;

import com.jcabi.aspects.Loggable;

/**
 * interface to define different access level checks for various resources.
 *  
 */
public interface ResourceAccessEvaluator {

     /**
      * To evaluate sku & permission validation for given resource
      * 
     * @param authentication
     * @param resource
     * @param permission
     * @param objectId
     * @return
     */
    @Loggable(value=Loggable.DEBUG)
    default public boolean evaluate(Authentication authentication,
            String resource, String permission, Serializable objectId) {
        
        return hasAdminAccess(authentication, resource, permission, objectId) ||
                hasFirstLevelAdminAccess(authentication, resource, permission, objectId) ||
                hasSecondLevelAdminAccess(authentication, resource, permission, objectId) ||
                hasSelfAccess(authentication, resource, permission, objectId);                            
    }

    /**
     * To evaluate whether logged in user has Admin level access to resource for given permission
     * 
     * @param authentication
     * @param resource
     * @param permission
     * @param objectId
     * @return
     */
    public boolean hasAdminAccess(Authentication authentication,
            String resource, String permission, Serializable objectId);

    /**
     * To evaluate whether logged in user has First level (Broker Admin, Broker Partner) access to resource for given permission and objectId
     * 
     * @param authentication
     * @param resource
     * @param permission
     * @param objectId
     * @return
     */
    public boolean hasFirstLevelAdminAccess(Authentication authentication,
            String resource, String permission, Serializable objectId);
    
    /**
     * To evaluate whether logged in user has Second level (RE, RE Admin) access to resource for given permission and objectId
     * 
     * @param authentication
     * @param resource
     * @param permission
     * @param objectId
     * @return
     */
    public boolean hasSecondLevelAdminAccess(Authentication authentication,
            String resource, String permission, Serializable objectId);

    /**
     * To evaluate whether logged in user has Self level (Student, Employee) access to resource for given permission and objectId
     * 
     * @param authentication
     * @param resource
     * @param permission
     * @param objectId
     * @return
     */
    public boolean hasSelfAccess(Authentication authentication,
            String resource, String permission, Serializable objectId);

}
