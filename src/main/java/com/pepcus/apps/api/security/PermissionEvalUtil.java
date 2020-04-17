package com.pepcus.apps.api.security;

import static com.pepcus.apps.api.constant.ApplicationConstants.BROKER_ID_PARAM;
import static com.pepcus.apps.api.constant.ApplicationConstants.CHILD_LEVEL_PERMISSION;
import static com.pepcus.apps.api.constant.ApplicationConstants.DOT;
import static com.pepcus.apps.api.constant.ApplicationConstants.PERMISSION_PREFIX;
import static com.pepcus.apps.api.constant.ApplicationConstants.REQUEST_PARAMETERS;
import static com.pepcus.apps.api.constant.ApplicationConstants.SELF_ACCESS_PERMISSION;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import com.pepcus.apps.api.exception.APIErrorCodes;
import com.pepcus.apps.api.exception.ApplicationException;
import com.pepcus.apps.api.model.AppAuthData;
import com.pepcus.apps.api.utils.RequestUtils;

public class PermissionEvalUtil {

    /**
     * @param collection
     * @param string
     * @return
     */
    public static <T> boolean hasPermission(Collection<? extends GrantedAuthority> availableAuthorities,
            List<String> requiredPermissions) {
        if (CollectionUtils.isEmpty(availableAuthorities)) {
            AppAuthData appAuthData = RequestUtils.getAppAuthData();
            if (appAuthData.getPermissions() != null) {
                for ( String perm : appAuthData.getPermissions()) {
                    if (requiredPermissions.contains(perm)) {
                        return true;
                    }
                }
            }
        }
        
        for (GrantedAuthority authority : availableAuthorities) {
            if (requiredPermissions.contains(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param collection
     * @param string
     * @return
     */
    public static <T> boolean hasPermission(Collection<? extends GrantedAuthority> availableAuthorities,
            String requiredPermission) {
        
        if (CollectionUtils.isEmpty(availableAuthorities)) {
            AppAuthData appAuthData = RequestUtils.getAppAuthData();
            if (appAuthData.getPermissions() != null) {
                return appAuthData.getPermissions().contains(requiredPermission);
            }
        }
        
        for (GrantedAuthority authority : availableAuthorities) {
            if (requiredPermission.equalsIgnoreCase(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
    
    public static List<String> getRequiredAdminAccessOnResource(String resource, String access) {
        List<String> keys = new ArrayList<>();
        keys.add(PERMISSION_PREFIX + DOT + resource);
        keys.add(PERMISSION_PREFIX + DOT + resource + DOT + access);
        return keys;
    }
    
    /**
     * @param resource
     * @return
     */
    public static String getRequiredParentPermissionOnResource(String resource) {
        return PERMISSION_PREFIX + DOT + resource + DOT + CHILD_LEVEL_PERMISSION;
    }

    /**
     * @param resource
     * @param access
     * @return
     */
    public static String getRequiredParentPermissionOnResourceAndAccess(String resource, String access) {
        return PERMISSION_PREFIX + DOT + resource + DOT + access + DOT + CHILD_LEVEL_PERMISSION;
    }

    /**
     * @param resource
     * @param permission
     * @return
     */
    public static String getRequiredSelfPermissionOnResourceAndAccess(String resource, String permission) {
        return PERMISSION_PREFIX + DOT +
                resource + DOT +
                permission + DOT + 
                SELF_ACCESS_PERMISSION;
    }

    /**
     * @param requestParamMap
     * @param paramName
     * @return
     */
    public static Integer getFromRequestParam(String paramName) {
        Map<String, String[]> requestParamMap = (Map<String, String[]>) RequestUtils
                .getRequestAttribute(REQUEST_PARAMETERS);
        
        if (requestParamMap == null) {
            return null;
        }
        
        String[] values = requestParamMap.get(paramName);
        if (values == null || values.length <=0) {
            return null;
        }
        try {
            return Integer.parseInt(values[0]);
        } catch (NumberFormatException ex) {
            throw ApplicationException.createBadRequest(APIErrorCodes.ARGUMENT_TYPE_MISMATCH, paramName 
                    + " should be a valid integer");
        }
    }

    /**
     * To get value of brokerId from request parameters
     * @return
     */
    public static Integer getBrokerIdFromToken() {
        AppAuthData authData = RequestUtils.getAppAuthData();
        return authData.getBrokerId();
    }

    /**
     * To get value of companyId from request parameters
     * @return
     */
    public static Integer getCompanyIdFromToken() {
        AppAuthData authData = RequestUtils.getAppAuthData();
        return authData.getClientId();
    }

    /**
     * To validate and authorize broker Id from request
     * @return
     */
    public static boolean isValidAndAuthorizedBrokerInRequest() {

        AppAuthData authData = RequestUtils.getAppAuthData();
        Integer brokerIdFromRequest = getFromRequestParam(BROKER_ID_PARAM);
        Integer brokerIdFromToken = authData.getBrokerId();
       
        if (null != brokerIdFromRequest) {
            return brokerIdFromRequest.equals(brokerIdFromToken);
        }
        
        return true;
    }

    /**
     * To validate and authorize companyId from request
     * @return
     */
    public static boolean isValidAndAuthorizedCompanyInRequest() {
        
        AppAuthData authData = RequestUtils.getAppAuthData();

        Integer companyIdFromRequest = getFromRequestParam("companyId");
        Integer companyFromJWT = authData.getClientId();

        if (companyIdFromRequest != null) {
            return companyIdFromRequest.equals(companyFromJWT);
        }

        return true;
    }

}
