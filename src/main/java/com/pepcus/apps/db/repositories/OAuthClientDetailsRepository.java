package com.pepcus.apps.db.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.pepcus.apps.db.entities.OAuthTenantDetailsEntity;

/**
 * OAuthClientDetails repository for "app_throne_oauth_client_details" entity.
 *  
 */
public interface OAuthClientDetailsRepository extends PagingAndSortingRepository<OAuthTenantDetailsEntity, Integer> {

    /**
     * Find Active Registered Brokers/Clients for given clientId
     * 
     * @param clientId
     * @param isActive
     * @return
     */
    public OAuthTenantDetailsEntity findByClientIdAndIsActive(String clientId, Boolean isActive);
    
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
    public OAuthTenantDetailsEntity findByClientSecretAndIsActive(String clientSecret, Boolean isActive);

}
