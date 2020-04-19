package com.pepcus.apps.db.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pepcus.apps.utils.PrepareStatementUtil;

import lombok.Data;

/**
 * This repository to handle bulk processing with an entity, like 
 * creation/deletion of records into batches
 * 
 * @author sandeep.vishwakarma
 *
 */
@Repository
@Data
public class BatchEntityRepository {

    @Autowired
    @Qualifier("springDataJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    

    /**
     * @param userId
     * @param subscriptionValues
     * @param targetList
     */
    @Transactional
    public void saveUser() {
        //jdbcTemplate.update(PrepareStatementUtils.buildPreparedStatementCreator());
    }

}
