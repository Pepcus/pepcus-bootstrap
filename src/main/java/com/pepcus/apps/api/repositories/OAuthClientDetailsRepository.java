package com.pepcus.apps.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.pepcus.apps.api.db.entities.OAuthClientDetails;

/**
 * OAuthClientDetails repository for "app_throne_oauth_client_details" entity.
 *  
 */
public interface OAuthClientDetailsRepository extends PagingAndSortingRepository<OAuthClientDetails, Integer> {

    /**
     * Find Active Registered Brokers/Clients for given clientId
     * 
     * @param clientId
     * @param isActive
     * @return
     */
    public OAuthClientDetails findByClientIdAndIsActive(String clientId, String isActive);
    
    /**
     * Find All registered redirect URL's
     * 
     * @param isActive
     * @return
     */
    @Query("select redirectUrl from OAuthClientDetails where isActive=?1")
    public List<String> findRedirectUrlByIsActive(String isActive);

    /**
     * Find Registered Brokers/Clients for given brokerId
     * 
     * @param brokerId
     * @return
     */
    public OAuthClientDetails findByBrokerId(Integer brokerId);
    
    /**
     * Find Active Registered Brokers/Clients for given clientSecret
     * 
     * @param clientSecret
     * @param isActive
     * @return
     */
    public OAuthClientDetails findByClientSecretAndIsActive(String clientSecret, String isActive);

}
