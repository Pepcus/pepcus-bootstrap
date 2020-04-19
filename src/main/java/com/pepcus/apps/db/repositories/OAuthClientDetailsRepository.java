package com.pepcus.apps.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.pepcus.apps.db.entities.OAuthClientDetailsEntity;

/**
 * OAuthClientDetails repository for "app_throne_oauth_client_details" entity.
 *  
 */
public interface OAuthClientDetailsRepository extends PagingAndSortingRepository<OAuthClientDetailsEntity, Integer> {

    /**
     * Find Active Registered Brokers/Clients for given clientId
     * 
     * @param clientId
     * @param isActive
     * @return
     */
    public OAuthClientDetailsEntity findByClientIdAndIsActive(String clientId, String isActive);
    
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
    //public OAuthClientDetails findByBrokerId(Integer brokerId);
    
    /**
     * Find Active Registered Brokers/Clients for given clientSecret
     * 
     * @param clientSecret
     * @param isActive
     * @return
     */
    public OAuthClientDetailsEntity findByClientSecretAndIsActive(String clientSecret, String isActive);

}
