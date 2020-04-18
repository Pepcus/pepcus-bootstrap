package com.pepcus.apps.api.repositories;

import com.pepcus.apps.api.db.entities.Role;

/**
 * User repository for Login entity.
 *  
 */
public interface RoleRepository extends BaseRepository<Role, Integer> {
    
    
    public Role findByKeyIgnoreCase(String roleKey);
    
    public Role findByNameIgnoreCase(String roleName);
    
   

}