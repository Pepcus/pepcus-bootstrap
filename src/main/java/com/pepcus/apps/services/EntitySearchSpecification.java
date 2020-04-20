package com.pepcus.apps.services;

import static com.pepcus.apps.utils.EntitySearchUtil.convertToDate;
import static com.pepcus.apps.utils.EntitySearchUtil.isDateField;
import static com.pepcus.apps.utils.EntitySearchUtil.isStringField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import com.pepcus.apps.db.entities.SearchableEntity;
import lombok.Data;

/**
 * Extends Specification specific to SearchableEntity
 * 
 * @author Sandeep.Vishwakarma
 *
 * @param <T>
 */
@Data
public class EntitySearchSpecification<T extends SearchableEntity> implements Specification<T> {

  protected String searchSpec;
  private Map<String, String> searchParameters;
  private boolean brokerOnly = false;
  private T t;

  /**
   * Constructor to create Entity Search Specification
   * 
   * @param searchSpec
   * @param entity
   */
  public EntitySearchSpecification(T entity) {
    super();
    this.t = entity;
  }

  /**
   * Constructor to create Entity Search Specification
   * 
   * @param searchParams
   * @param entity
   */
  public EntitySearchSpecification(Map<String, String> searchParams, T entity) {
    super();
    this.searchParameters = searchParams;
    this.t = entity;
  }

  @Override
  public Predicate toPredicate(Root<T> from, CriteriaQuery<?> criteria, CriteriaBuilder criteriaBuilder) {

    List<Predicate> predicates = new ArrayList<Predicate>();

    // WHEN search spec is not null
    if (StringUtils.isNotBlank(searchSpec)) {
      Predicate searchPredicate = buildSearchSpecPredicate(from, criteriaBuilder);
      predicates.add(searchPredicate);
    }

    Predicate[] pr = new Predicate[predicates.size()];
    predicates.toArray(pr);
    return criteriaBuilder.and(pr);

  }

  /**
   * To build search spec predicate
   * 
   * @param from
   * @param criteriaBuilder
   * @return
   */
  protected Predicate buildSearchSpecPredicate(Root<T> from, CriteriaBuilder criteriaBuilder) {
    Predicate searchPredicate = criteriaBuilder.disjunction();
    List<String> searchColumns = t.getSearchFields();

    searchColumns.stream().forEach(column -> {
      searchPredicate.getExpressions().add(criteriaBuilder.like(from.get(column), "%" + searchSpec + "%"));
    });
    return searchPredicate;
  }

  private Predicate createPredicate(Root<T> from,
      CriteriaBuilder criteriaBuilder,
      Map<String, String> requestParameters) {

    Predicate filterPredicate = criteriaBuilder.conjunction();
    requestParameters.entrySet().forEach(searchParam -> {
      Class klass = t.getClass();
      Path<String> searchKeyPath = null;
      if (isStringField(klass, searchParam.getKey())) {
        filterPredicate.getExpressions().add(criteriaBuilder.like(searchKeyPath, "%" + searchParam.getValue() + "%"));
      } else if (isDateField(klass, searchParam.getKey())) {
        Date date = convertToDate(searchParam.getValue(), searchParam.getKey(), false);
        filterPredicate.getExpressions().add(criteriaBuilder.equal(searchKeyPath, date));
      } else {
        filterPredicate.getExpressions().add(criteriaBuilder.equal(searchKeyPath, searchParam.getValue()));
      }
    });
    return filterPredicate;
  }
}
