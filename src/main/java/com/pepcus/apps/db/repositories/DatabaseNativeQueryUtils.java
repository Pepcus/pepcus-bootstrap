package com.pepcus.apps.db.repositories;

/**
 * This class is to keep all the native queries. These queries will mainly required for bulk operations 
 * that can improve performance by reducing extra joins/query if we us Spring JPA based Data access layer. 
 * 
 */
public class DatabaseNativeQueryUtils {

    private static final String INSERT_LEARN_USER = "INSERT INTO mdl_user";
}
