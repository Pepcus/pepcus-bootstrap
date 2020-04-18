package com.pepcus.apps.security.oauth2.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.CollectionUtils;

import com.pepcus.apps.constant.ApplicationConstants;
import com.pepcus.apps.db.entities.OAuthClientDetails;
import com.pepcus.apps.db.entities.RoleEntity;
import com.pepcus.apps.db.entities.UserEntity;
import com.pepcus.apps.db.repositories.OAuthClientDetailsRepository;
import com.pepcus.apps.db.repositories.RoleRepository;
import com.pepcus.apps.db.repositories.UserRepository;
import com.pepcus.apps.exception.APIErrorCodes;
import com.pepcus.apps.exception.ApplicationException;
import com.pepcus.apps.model.AppAuthData;
import com.pepcus.apps.utils.JWTUtils;

import lombok.Data;

/**
 * This class is to enhance JwtToken to handle application specific additional parameters 
 * 
 * @author sandeep.vishwarkarma
 *
 */
@Data
public class AppTokenEnhancer extends JwtAccessTokenConverter {
    
    private Map<String, Object> additionalInfo;

    @Value("${JWT.jwt_iss}")
    private String issuer;
    
    @Value("${app.default.tenant}")
    private String tenantKey;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private OAuthClientDetailsRepository authClientDetailsRepository;
    
    /**
     * To Define how authorization server generate access token, in case add some addition information
     */
    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {
        
        setAdditionalInfo(authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        
        accessToken = super.enhance(accessToken, authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(Collections.emptyMap());
        
        return accessToken;
    }
    
    /**
     * Prepare AppAuthData from token 
     * @param token
     * @return
     */
    public AppAuthData getAppAuthData (String token) {
        AppAuthData appAuthData = JWTUtils.prepareAuthToken(decode(token));
        
        if (!issuer.equalsIgnoreCase(appAuthData.getIss())) {
            throw ApplicationException.createAuthorizationError(APIErrorCodes.AUTHORIZATION_FAILED, "issuer = "+ issuer);
        }
        // Validate user
        if (StringUtils.isNotBlank(appAuthData.getUser())) {
            UserEntity user = userRepository.findByLogin_UserName(appAuthData.getUser());
            if (null == user){
                throw ApplicationException.createAuthorizationError(APIErrorCodes.AUTHORIZATION_FAILED, "user = "+ appAuthData.getUser());
            }
            appAuthData.setUserId(user.getId()); 
        }

        RoleEntity role = validatePermissions(appAuthData);
        
        // Load Permissions
        role.getPermissions().forEach(perm -> appAuthData.addPermission(perm.getKey()));
        
        return appAuthData;

    }
    
    /**
     * To add additional informations to access token in addition to default attributes.
     * 
     * @param userDetails
     */
    private void setAdditionalInfo(OAuth2Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        
        UserEntity user = userRepository.findByLogin_UserName(userDetails.getUsername());
        
        String clientId = authentication.getOAuth2Request().getClientId(); 
        OAuthClientDetails authClientDetails = authClientDetailsRepository.findByClientIdAndIsActive(clientId,"1");
        
        // Commented by sandeep : need to fix
        if (!authClientDetails.getTenantKey().equals(tenantKey)) {
        	throw new InvalidClientException("Bad Credentials");
        }
        
        //In case any additional informations are required
        additionalInfo = new HashMap<String, Object>();
        additionalInfo.put(ApplicationConstants.JWT_TOKEN_ISS, issuer);
        additionalInfo.put(ApplicationConstants.JWT_TOKEN_TENANT_KEY, user.getTenant().getKey());
        if (user.getUsername() != null) {
            additionalInfo.put(ApplicationConstants.JWT_TOKEN_USER, user.getUsername());
        }
        additionalInfo.put(ApplicationConstants.JWT_TOKEN_ROLE, user.getRole().getName());
        
        if (user.getId() != null) { 
            additionalInfo.put(ApplicationConstants.JWT_TOKEN_SUB, user.getId());
        } 
    }
    
    /**
     * To validate permissions.
     * 
     * @param authData
     * @return
     */
    private RoleEntity validatePermissions(AppAuthData authData) {
    	RoleEntity throneRole = roleRepository.findByNameAndTenantKey(authData.getRole(), authData.getTenantKey());
        if (CollectionUtils.isEmpty(throneRole.getPermissions())) {
            throw ApplicationException.createAuthorizationError(APIErrorCodes.AUTHORIZATION_FAILED, 
            		"role = "+ authData.getRole() + " have not assigned any permission");
        } 

        return throneRole;
    }
 
}