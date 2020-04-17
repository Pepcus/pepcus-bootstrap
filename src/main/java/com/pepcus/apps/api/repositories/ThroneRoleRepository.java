package com.pepcus.apps.api.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pepcus.apps.api.db.entities.ThroneRole;


/**
 * LearnRole repository for LearnRole entity.
 *  
 */

public interface ThroneRoleRepository extends CrudRepository<ThroneRole, Integer> {

    public ThroneRole findFirstByNameAndTypeAndBrokerId(String name, String type, Integer brokerId);
    
    @Query(value = "select * from app_throne_roles where name = :name and brokerId is null or name = :name and brokerId = :brokerId", nativeQuery = true)
    public ThroneRole findByNameAndBrokerId(@Param("name") String name, @Param("brokerId") Integer brokerId);
}