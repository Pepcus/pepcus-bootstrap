package com.pepcus.apps.db.repositories;

import com.pepcus.apps.db.entities.TenantEntity;

/**
 * User repository for Login entity.
 *  
 */
public interface TenantRepository extends BaseRepository<TenantEntity, Integer> {
    
    public TenantEntity findByTenantName(String tenantName);

    public TenantEntity findByTenantKey(String tenantKey);
}