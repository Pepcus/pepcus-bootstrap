package com.pepcus.apps.db.repositories;

import com.pepcus.apps.db.entities.RoleEntity;

/**
 * Role repository for Role entity
 *  
 */
public interface RoleRepository extends BaseRepository<RoleEntity, Integer> {

	RoleEntity findByNameAndTenantKey(String role, String tenantKey);
    
}