package com.pepcus.apps.db.repositories;

import com.pepcus.apps.db.entities.RoleEntity;

/**
 * Repository for "Role" entity.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public interface RoleRepository extends BaseRepository<RoleEntity, Integer> {

  RoleEntity findByNameAndTenantKey(String role, String tenantKey);

}
