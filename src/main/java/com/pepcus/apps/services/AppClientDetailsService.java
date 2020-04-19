package com.pepcus.apps.services;

import static com.pepcus.apps.constant.ApplicationConstants.AUTHORIZATION_CODE_GRANT_TYPE;
import static com.pepcus.apps.constant.ApplicationConstants.IMPLICIT_GRANT_TYPE;
import static com.pepcus.apps.constant.ApplicationConstants.OPEN_ID_GRANT_TYPE;
import static com.pepcus.apps.constant.ApplicationConstants.REFRESH_TOKEN_GRANT_TYPE;
import static com.pepcus.apps.constant.ApplicationConstants.RESOURCE_OWNER_GRANT_TYPE;
import static com.pepcus.apps.constant.ApplicationConstants.SCOPE_ALL;
import java.util.Arrays;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import com.pepcus.apps.db.entities.OAuthTenantDetailsEntity;
import com.pepcus.apps.db.repositories.OAuthClientDetailsRepository;

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
        OAuthTenantDetailsEntity authDetails = authClientDetailsRepository.findByClientIdAndIsActive(clientId,true);
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
        if (authDetails.getRedirectUri() != null) {
            clientDetails.setRegisteredRedirectUri(new HashSet<String>(Arrays.asList(authDetails.getRedirectUri())));
        }
        return clientDetails;
    }

}