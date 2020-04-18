package com.pepcus.apps.db.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T>{
    
    /**
     * Returns a {@link org.springframework.data.domain.Page} of ids matching the given {@link com.mysema.query.types.Predicate}.
     * This also uses provided projections ( can be JavaBean or constructor or anything supported by QueryDSL
     * @param constructorExpression this constructor expression will be used for transforming query results
     * @param predicate
     * @param pageable
     * @return
     */
    Page<Integer> findAllIds(Specification<T> spec, Pageable pageable, String attributeName);

    /**
     * Returns sorted entity data for specific ids.
     * 
     * @param sort
     * @param attributeName
     * @param ids
     * @return
     */
    List<T> findById(Sort sort, String attributeName, List<Integer> ids);
    
    /**
     * Returns sorted entity list.
     * 
     * @param spec
     * @param fields
     * @param pageable
     * @param idFieldName
     * @param isBroker
     * @return
     */
    List<Object[]> findResources(Specification<T> spec, List<String> fields, Pageable pageable, String idFieldName, boolean isBroker);
    
    /**
     * Returns entity data for specific id.
     * 
     * @param spec
     * @param fields
     * @param pageable
     * @param idFieldName
     * @param isBroker
     * @return
     */
    List<Object[]> findResource(Integer id, List<String> fields, String iFieldName, boolean isBroker);

}
