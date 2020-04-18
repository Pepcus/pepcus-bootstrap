package com.pepcus.apps.api.repositories;

import com.pepcus.apps.api.db.entities.Tenant;

/**
 * User repository for Login entity.
 *  
 */
public interface TenantRepository extends BaseRepository<Tenant, Integer> {
    
    
    public Tenant findByKeyIgnoreCase(String tenantKey);
    
    public Tenant findByNameIgnoreCase(String tenantName);
    
   

}