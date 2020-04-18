package com.pepcus.apps.api.security.oauth2;

import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.CollectionUtils;
import com.pepcus.apps.api.db.entities.Contact;
import com.pepcus.apps.api.db.entities.OAuthClientDetails;
import com.pepcus.apps.api.db.entities.ThroneRole;
import com.pepcus.apps.api.db.entities.User;
import com.pepcus.apps.api.exception.APIErrorCodes;
import com.pepcus.apps.api.exception.ApplicationException;
import com.pepcus.apps.api.model.AppAuthData;
import com.pepcus.apps.api.repositories.OAuthClientDetailsRepository;
import com.pepcus.apps.api.repositories.ThroneRoleRepository;
import com.pepcus.apps.api.repositories.UserRepository;
import com.pepcus.apps.api.utils.JWTUtils;
import lombok.Data;

@Data
public class AppTokenEnhancer extends JwtAccessTokenConverter {
    
    private Map<String, Object> additionalInfo;

    @Value("${JWT.jwt_iss}")
    private String issuer;
    
    @Value("${app.default.tenant}")
    private Integer tenantId;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ThroneRoleRepository roleRepository;
    
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
            User user = userRepository.findByLogin_UserName(appAuthData.getUser());
            if (null == user){
                throw ApplicationException.createAuthorizationError(APIErrorCodes.AUTHORIZATION_FAILED, "user = "+ appAuthData.getUser());
            }
            appAuthData.setUserId(user.getId()); 
        }

        ThroneRole throneRole = validatePermissions(appAuthData);
        
        // Load Permissions
        throneRole.getPermissions().forEach(perm -> appAuthData.addPermission(perm.getPermissionKey()));
        
        return appAuthData;

    }
    
    /**
     * To add additional informations to access token in addition to default attributes.
     * 
     * @param userDetails
     */
    private void setAdditionalInfo(OAuth2Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        
        User user = userRepository.findByLogin_UserName(userDetails.getUsername());
        
        String clientId = authentication.getOAuth2Request().getClientId(); 
        OAuthClientDetails authClientDetails = authClientDetailsRepository.findByClientIdAndIsActive(clientId,"1");
        
        // Commented by sandeep : need to fix
        /*if (!authClientDetails.getBrokerId().equals(tenantId)) {
        	throw new InvalidClientException("Bad Credentials");
        }*/
        
        //In case any additional informations are required
       /* additionalInfo = new HashMap<String, Object>();
        additionalInfo.put(JWT_TOKEN_THR_ISS, issuer);
        additionalInfo.put(JWT_TOKEN_THR_CLIENT_ID, appUser.getCompanyId());
        if (appUser.getUserName() != null) {
            additionalInfo.put(JWT_TOKEN_THR_USER, appUser.getUserName());
        }
        additionalInfo.put(JWT_TOKEN_THR_ROLE, role.getName());
        if (appUser.getUserId() != null) { 
            additionalInfo.put(JWT_TOKEN_THR_SUB, appUser.getUserId());
        } */
    }
    
    /**
     * To validate permissions.
     * 
     * @param authData
     * @return
     */
    private ThroneRole validatePermissions(AppAuthData authData) {
        ThroneRole throneRole = roleRepository.findByNameAndBrokerId(authData.getRole(), authData.getBrokerId());
        if (CollectionUtils.isEmpty(throneRole.getPermissions())) {
            throw ApplicationException.createAuthorizationError(APIErrorCodes.AUTHORIZATION_FAILED, "role = "+ authData.getRole() + " have not assigned any permission");
        } 

        return throneRole;
    }
 
}