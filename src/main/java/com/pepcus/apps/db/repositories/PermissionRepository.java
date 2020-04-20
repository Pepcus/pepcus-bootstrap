package com.pepcus.apps.db.repositories;

import org.springframework.data.repository.CrudRepository;
import com.pepcus.apps.db.entities.PermissionEntity;

/**
 * Repository for "Permisson" entity.
 * 
 * @author sandeep.vishwakarma
 *
 */
public interface PermissionRepository extends CrudRepository<PermissionEntity, Integer> {

}
