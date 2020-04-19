package com.pepcus.apps.db.repositories;

import com.pepcus.apps.db.entities.TenantEntity;

/**
 * User repository for Login entity.
 *  
 */
public interface TenantRepository extends BaseRepository<TenantEntity, Integer> {
    
    public TenantEntity findByName(String tenantName);

    public TenantEntity findByKey(String tenantKey);
}