package com.pepcus.apps.api.services;

import static com.pepcus.apps.api.constant.ApplicationConstants.AUTHORIZATION_CODE_GRANT_TYPE;
import static com.pepcus.apps.api.constant.ApplicationConstants.BROKER_ID_PARAM;
import static com.pepcus.apps.api.constant.ApplicationConstants.BROKER_NAME;
import static com.pepcus.apps.api.constant.ApplicationConstants.IMPLICIT_GRANT_TYPE;
import static com.pepcus.apps.api.constant.ApplicationConstants.ISSUER;
import static com.pepcus.apps.api.constant.ApplicationConstants.JWK_URL;
import static com.pepcus.apps.api.constant.ApplicationConstants.MAPPED_FIELD;
import static com.pepcus.apps.api.constant.ApplicationConstants.OPEN_ID_GRANT_TYPE;
import static com.pepcus.apps.api.constant.ApplicationConstants.REFRESH_TOKEN_GRANT_TYPE;
import static com.pepcus.apps.api.constant.ApplicationConstants.RESOURCE_OWNER_GRANT_TYPE;
import static com.pepcus.apps.api.constant.ApplicationConstants.SCOPE_ALL;
import static com.pepcus.apps.api.constant.ApplicationConstants.USERINFO_URL;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import com.pepcus.apps.api.db.entities.OAuthClientDetails;
import com.pepcus.apps.api.repositories.OAuthClientDetailsRepository;
import com.pepcus.apps.api.utils.RequestUtils;

/**
 * Class used to load "oauth_client_details" object from Database
 * 
 */
@Service
public class AppClientDetailsService implements ClientDetailsService {
    
    @Value("${com.pepcus.oauth.security.access.token.validity.seconds}")
    private Integer accessTokenValiditySeconds;
    
    @Value("${com.pepcus.oauth.security.refresh.token.validity.seconds}")
    private Integer refreshTokenValiditySeconds;
    
    @Autowired
    private OAuthClientDetailsRepository authClientDetailsRepository;
    
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        OAuthClientDetails authDetails = authClientDetailsRepository.findByClientIdAndIsActive(clientId,"1");
        if (authDetails == null) {
            throw new InvalidClientException("Bad Credentials");
        }
        
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(authDetails.getClientId());
        clientDetails.setClientSecret(authDetails.getClientSecret());
        clientDetails.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        clientDetails.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        clientDetails.setScope(Arrays.asList(SCOPE_ALL));
        clientDetails.setAuthorizedGrantTypes(Arrays.asList(IMPLICIT_GRANT_TYPE, 
                                                            RESOURCE_OWNER_GRANT_TYPE, 
                                                            REFRESH_TOKEN_GRANT_TYPE,
                                                            AUTHORIZATION_CODE_GRANT_TYPE,
                                                            OPEN_ID_GRANT_TYPE));
        if (authDetails.getRedirectUrl() != null) {
            clientDetails.setRegisteredRedirectUri(new HashSet<String>(Arrays.asList(authDetails.getRedirectUrl())));
        }
        return clientDetails;
    }

}