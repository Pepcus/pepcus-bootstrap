package com.pepcus.apps.db.repositories;

import com.pepcus.apps.db.entities.UserEntity;

/**
 * Repository for "User" entity.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public interface UserRepository extends BaseRepository<UserEntity, Integer> {

  public UserEntity findByUsername(String username);



}
