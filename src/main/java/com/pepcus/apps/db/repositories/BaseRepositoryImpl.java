package com.pepcus.apps.db.repositories;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.repository.support.PageableExecutionUtils.TotalSupplier;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Base repository class to improve performance on list operations. Here mainly it pull ids for all resources filtered by specifications
 * and then based on their ids, pull all/few fields based on request.
 * 
 * @author sandeep.vishwakarma
 *
 * @param <T>
 * @param <ID>
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    
    protected final EntityManager entityManager;
    
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }
    
    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
      }
    
    @Override
    public Page<Integer> findAllIds(Specification<T> spec, Pageable pageable, String attributeName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getDomainClass());
        Root<T> root = query.from(getDomainClass());
        query.select(getSearchSelection(root, attributeName, getDomainClass()));
        Sort sort = pageable == null ? null : pageable.getSort();
        
        if (spec != null) {
            query.where(spec.toPredicate(root, query, criteriaBuilder));
        }

        if (sort != null) {
            query.orderBy(toOrders(sort, root, criteriaBuilder));
        }
        
        return pageable == null ? (Page<Integer>) new PageImpl<T>(entityManager.createQuery(query).getResultList())
                : readCustomPage(entityManager.createQuery(query), getDomainClass(), pageable, spec);
    }
    
    @Override
    public List<T> findById(Sort sort, String attributeName, List<Integer> ids) {
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getDomainClass());
        Root<T> root = query.from(getDomainClass());
        Expression<String> parentExpression = root.get(attributeName);
        Predicate inPredicate = parentExpression.in(ids);
        query.where(inPredicate);
        
        if (sort != null) {
            query.orderBy(toOrders(sort, root, criteriaBuilder));
        }
        
        return entityManager.createQuery(query).getResultList();
    }

	/**
     * To Apply pagination
     * 
     * @param query
     * @param class1
     * @param pageable
     * @param spec
     * @return
     */
    private Page<Integer> readCustomPage(TypedQuery<T> query,
            Class<T> class1, Pageable pageable, Specification<T> spec) {

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return (Page<Integer>) PageableExecutionUtils.getPage(query.getResultList(), pageable, new TotalSupplier() {

            @Override
            public long get() {
                return executeCountQuery(getCountQuery(spec, getDomainClass()));
            }
        });
    }

    /**
     * Custom Implementation of count query with query caching enabled
     * 
     * @param query
     * @return
     */
    protected long executeCountQuery(TypedQuery<Long> query) {
        
        Assert.notNull(query, "TypedQuery must not be null!");
        query.setHint("org.hibernate.cacheable", true);
        List<Long> totals = query.getResultList();
        Long total = 0L;

        for (Long element : totals) {
            total += element == null ? 0 : element;
        }

        return total;
    }
    
    @Override
    public List<Object[]> findResources(Specification<T> spec, List<String> fields, Pageable pageable, String idFieldName, boolean isBroker) {
        
        Page<Integer> ids = findAllIds(spec, pageable, idFieldName);
        
        if (CollectionUtils.isEmpty(ids.getContent())) {
            return Collections.EMPTY_LIST;
        }
        
        return findResourcesWithSpecificFields(fields, pageable, idFieldName, isBroker, ids.getContent(), ids.getTotalElements());
    }

    /**
     * @param fields
     * @param pageable
     * @param idFieldName
     * @param isBroker
     * @param list
     * @return
     */
    private List<Object[]> findResourcesWithSpecificFields(List<String> fields, Pageable pageable, String idFieldName, boolean isBroker, List<Integer> ids, long totalElements) {
        List<Predicate> predicates = new ArrayList<Predicate>(); 
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Object[]> query = criteriaBuilder.createQuery(Object[].class);
        Class<T> domainClass = getDomainClass();
        Root<T> root = query.from(domainClass);
        
        query.multiselect(fields.stream().map(attribute -> getSearchSelection(root, attribute, domainClass)).collect(Collectors.toList()));

        Expression<String> parentExpression = root.get(idFieldName);
        predicates.add(parentExpression.in(ids));
        
        Predicate[] pr = new Predicate[predicates.size()];
        predicates.toArray(pr);
        query.where(criteriaBuilder.and(pr)); 
        
        if (pageable != null && pageable.getSort() != null) {
            query.orderBy(toOrders(pageable.getSort(), root, criteriaBuilder));
        }
        
        Object[] totalElementsObject = {totalElements};
        
        List<Object[]> results = entityManager.createQuery(query).getResultList();
        
        results.add(totalElementsObject);
        
        return results;
    }

    /**
     * @param root
     * @param fieldName
     * @param kclass
     * @return
     */
    @SuppressWarnings("unchecked")
    private Path<T> getSearchSelection(Root<T> root, String fieldName, Class kclass) {
        return root.get(fieldName);
    }

    @Override
    public List<Object[]> findResource(Integer id, List<String> fields, String idFieldName, boolean isBroker) {
        
        return findResourcesWithSpecificFields(fields, null, idFieldName, isBroker, Arrays.asList(id), 1);
        
    }
}
