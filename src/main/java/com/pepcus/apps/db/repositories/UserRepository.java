package com.pepcus.apps.db.repositories;

import com.pepcus.apps.db.entities.UserEntity;

/**
 * User repository for user entity.
 *  
 */
public interface UserRepository extends BaseRepository<UserEntity, Integer> {
    
    //public final String isThroneAndActiveUser = "company.isThroneCompany=1 and isActive=1";
    //public final String isThroneUser = "company.isThroneCompany=1";
    public UserEntity findByLogin_UserName(String username);
    
   

}