package com.pepcus.apps.api.repositories;

import com.pepcus.apps.api.db.entities.Login;
import com.pepcus.apps.api.db.entities.User;

/**
 * User repository for Login entity.
 *  
 */
public interface LoginRepository extends BaseRepository<Login, Integer> {
    
    
    public User findByUserName(String username);
    
   

}