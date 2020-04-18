package com.pepcus.apps.db.entities;

import java.util.List;

/**
 * This interface to define all the abstractions that are required for an entity to search.
 * 
 */
public interface SearchableEntity {
	
    public List<String> getSearchFields();

    public String getNodeName();
    public String getMultiDataNodeName();
    
}
