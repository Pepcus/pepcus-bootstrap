package com.pepcus.apps.db.repositories;

import org.springframework.data.repository.CrudRepository;
import com.pepcus.apps.db.entities.PermissionEntity;

/**
 * @author sandeep.vishwakarma
 *
 */
public interface PermissionRepository extends CrudRepository<PermissionEntity, Integer> {

}
