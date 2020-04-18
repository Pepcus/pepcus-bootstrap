package com.pepcus.apps.api.repositories;

import com.pepcus.apps.api.db.entities.User;

/**
 * User repository for user entity.
 *  
 */
public interface UserRepository extends BaseRepository<User, Integer> {
    
    //public final String isThroneAndActiveUser = "company.isThroneCompany=1 and isActive=1";
    //public final String isThroneUser = "company.isThroneCompany=1";
    public User findByLogin_UserName(String username);
    
   

}