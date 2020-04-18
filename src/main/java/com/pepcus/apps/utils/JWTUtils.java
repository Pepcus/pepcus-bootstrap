package com.pepcus.apps.utils;

import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_ISS;
import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_ROLE;
import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_SUB;
import static com.pepcus.apps.constant.ApplicationConstants.JWT_TOKEN_USER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import com.pepcus.apps.model.AppAuthData;

/**
 *
 * Helper to decode a JWT token
 *
 *
 */

public class JWTUtils {

    /**
     * Get the values from map and set it to AppAuthToken
     * 
     * @param claims
     * @return
     */
    public static AppAuthData prepareAuthToken(Map<String, ?> tokenMap) {
        Object sub = tokenMap.get(JWT_TOKEN_SUB);
        Object user = tokenMap.get(JWT_TOKEN_USER);
        Object issClaim = tokenMap.get(JWT_TOKEN_ISS);
        Object role = tokenMap.get(JWT_TOKEN_ROLE);

        AppAuthData authToken = new AppAuthData();
        if (user != null) {
            authToken.setUser((String)user);
        }
        if (issClaim != null) {
            authToken.setIss((String)issClaim);
        }
        if (role != null) {
            authToken.setRole((String)role);
        }
        if (sub != null) {
            authToken.setSub((Integer)sub);
            authToken.setUserId((Integer)sub);
        }
        return authToken;
    }

    /**
     * 
     * @param role
     * @return
     */
    public static Collection<? extends GrantedAuthority> getAuthorities(List<String> permissionList) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (!CollectionUtils.isEmpty(permissionList)) { 
            permissionList.forEach(permission -> {
                if (StringUtils.isNotBlank(permission)) 
                    authorities.add(new SimpleGrantedAuthority(permission)); 
            });
        }
        return authorities;
    }

}
