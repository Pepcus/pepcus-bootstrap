package com.pepcus.apps.services;

import static com.pepcus.apps.constant.ApplicationConstants.CLOSE_OPEN_BRACKET;
import static com.pepcus.apps.constant.ApplicationConstants.REGEX_FOR_PROJECTION_FIELDS;
import static com.pepcus.apps.constant.ApplicationConstants.SMALL_OPEN_BRACKET;
import static com.pepcus.apps.constant.ApplicationConstants.TOTAL_RECORDS;
import static com.pepcus.apps.exception.APIErrorCodes.INVALID_FIELD_EXPRESSION;
import static com.pepcus.apps.exception.APIErrorCodes.INVALID_FIELD_NAME;
import static com.pepcus.apps.utils.EntitySearchUtil.classHasField;
import static com.pepcus.apps.utils.RequestUtil.setRequestAttribute;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.pepcus.apps.db.entities.SearchableEntity;
import com.pepcus.apps.db.repositories.BaseRepository;
import com.pepcus.apps.db.repositories.OAuthClientDetailsRepository;
import com.pepcus.apps.db.repositories.UserRepository;
import com.pepcus.apps.exception.ApplicationException;
import lombok.Data;
import lombok.extern.log4j.Log4j;

/**
 * Common Service to hold all general operations
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Service
@Data
@Log4j
public class CommonEntityService {

  @Autowired
  protected OAuthClientDetailsRepository oAuthClientDetailsRepository;

  @Autowired
  protected UserRepository userRepository;

  /**
   * @return
   */
  public String getDefaultSortField() {
    return null;
  }

  /**
   * This function overwrites values from given json string in to given objectToUpdate
   * 
   * @param json
   * @param objectToUpdate
   * @return
   * @throws IOException
   */
  public <T> T update(String json, T objectToUpdate) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDefaultMergeable(true); // This is required for deep update. Available in
                                            // jackson-databind from 2.9 version

    // This is required to disable the check for unknown properties. Otherwise
    // UnrecognizedPropertyException is thrown.
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    ObjectReader updater = objectMapper.readerForUpdating(objectToUpdate);

    return updater.readValue(json);
  }

  /**
   * Validates given object for any constraint violations and throws ConstraintViolationException if
   * any violations are found
   * 
   * @param object
   */
  public <T> void validateObject(T object) {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

    if (constraintViolations != null && !constraintViolations.isEmpty()) {
      ConstraintViolationException ex = new ConstraintViolationException(constraintViolations);
      throw ex;
    }
  }

  /**
   * Get required object from given json model
   * 
   * @param object
   * @return
   */
  public <T> Object getObjectFromJson(String json, Class<T> klass) {
    ObjectMapper mapper = new ObjectMapper();
    Object object = null;

    try {
      object = mapper.readValue(json, klass);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return object;

  }

  /**
   * Returns true if Json String has the requested field
   * 
   * @param requestJson
   * @return
   */
  public boolean containsField(String requestJson, String field) {

    try {
      ObjectMapper mapper = new ObjectMapper();
      // convert JSON string to Map
      JsonNode node = mapper.readTree(requestJson);
      return (node.has(field)) ? true : false;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the value of requested field in Json String
   * 
   * @param requestJson
   * @param field
   * @return
   */
  public String getFieldValue(String requestJson, String field) {

    try {
      ObjectMapper mapper = new ObjectMapper();
      // convert JSON string to Map
      JsonNode node = mapper.readTree(requestJson);
      return node.get(field).asText().trim();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * To get the field value if field name exists in request json.
   * 
   * @param requestJson
   * @param fieldName
   * @return
   */
  public String getFieldValueIfExists(String requestJson, String fieldName) {

    String fieldValue = null;

    if (containsField(requestJson, fieldName)) {
      fieldValue = getFieldValue(requestJson, fieldName);
    }
    return fieldValue;
  }

  /**
   * Find All Resources according to the specification
   * 
   * @param isBroker
   * @param pageable
   * @param spec
   * @return
   */
  protected <T> List<T> findAll(boolean isBroker,
      Pageable pageable,
      Specification<T> spec,
      Class klass,
      BaseRepository repository,
      String attributeName,
      Map<String, List<String>> classVsFieldMap) {

    Page<Integer> ids = repository.findAllIds(spec, pageable, attributeName);

    if (ids == null || ids.getTotalElements() == 0 || ids.getContent().isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    setRequestAttribute(TOTAL_RECORDS, ids.getTotalElements());
    List records = repository.findById(pageable.getSort(), attributeName, ids.getContent());

    if (null != classVsFieldMap && !classVsFieldMap.isEmpty()) {

      List<T> filteredList = new ArrayList<T>();
      records.forEach(c -> {
        filteredList.add((T) filterFields(c, klass, classVsFieldMap));
      });

      return filteredList;
    }

    return records;
  }


  /**
   * Find all unique id's for the resource with spec and pageable attributes
   * 
   * @param isBroker
   * @param pageable
   * @param spec
   * @param klass
   * @param repository
   * @param attributeName
   * @param classVsFieldMap
   * @return
   */
  protected List<Integer> findAllIds(Pageable pageable,
      Specification spec,
      BaseRepository repository,
      String attributeName) {
    return repository.findAllIds(spec, pageable, attributeName).getContent();
  }

  /**
   * Handle field values to filter the class
   * 
   * @param fields
   * @param kclass
   */
  public Map<String, List<String>> validateFieldsAndGetClassVsFieldMap(String fields, Class kclass) {
    Map<String, List<String>> classVsFieldMap = null;

    if (!StringUtils.isEmpty(fields)) {
      classVsFieldMap = new HashMap<String, List<String>>();
      prepareClassVsFieldsMap(fields, kclass, classVsFieldMap);
    }

    return classVsFieldMap;
  }

  /**
   * To parse field expression to support projections TODO: need to add validations
   * 
   * @param fields
   * @param kclass
   * @param classVsFieldMap
   */
  public void prepareClassVsFieldsMap(String fields, Class kclass, Map<String, List<String>> classVsFieldMap) {
    String[] fieldsArray = fields.split(REGEX_FOR_PROJECTION_FIELDS);
    List<String> fieldList = new ArrayList<String>();
    try {
      for (int i = 0; i < fieldsArray.length; i++) {
        String fieldItem = fieldsArray[i];
        if (fieldItem.contains(SMALL_OPEN_BRACKET)) {
          String nestedFields = new String(fieldItem);
          int openBracketIndex = nestedFields.indexOf(SMALL_OPEN_BRACKET);
          int closeBracketIndex = nestedFields.lastIndexOf(CLOSE_OPEN_BRACKET);
          String nestedClassName = nestedFields.substring(0, openBracketIndex);
          // if (!nestedClasses.containsKey(nestedClassName)) {
          // throw ApplicationException.createBadRequest(INVALID_FIELD_NAME, nestedClassName,
          // kclass.getSimpleName());
          // }
          // Class nestedClass = nestedClasses.get(nestedClassName);
          if (!classHasField(kclass, nestedClassName)) {
            throw ApplicationException.createBadRequest(INVALID_FIELD_NAME, nestedClassName, kclass.getSimpleName());
          }
          fieldList.add(nestedClassName);
          String nestedFieldArray = nestedFields.substring(openBracketIndex + 1, closeBracketIndex);
          // prepareClassVsFieldsMap(nestedFieldArray, nestedClass, classVsFieldMap);
        } else {
          if (!classHasField(kclass, fieldItem)) {
            throw ApplicationException.createBadRequest(INVALID_FIELD_NAME, fieldItem, kclass.getSimpleName());
          }
          fieldList.add(fieldItem);
        }
      }
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      throw ApplicationException.createBadRequest(INVALID_FIELD_EXPRESSION, fields, kclass.getSimpleName());
    }
    String classKey = kclass.getSimpleName().toLowerCase();
    classVsFieldMap.put(classKey, fieldList);
  }

  /**
   * Generic method to support projection with given fields. Supports nested objects. TODO: To add
   * support for second level nesting.
   * 
   * @param object
   * @param kclass
   * @param classVsFieldMap
   * @return
   */

  public Object filterFields(Object object, Class<?> kclass, Map<String, List<String>> classVsFieldMap) {
    SearchableEntity newObj = null;

    // TO handle skus which has containing class type : PersistentSet
    if (kclass.getName().contains("PersistentSet")) {
      Set<Object> nestedObjSet = (Set) object;
      Set<Object> newSet = new HashSet<Object>();
      for (Object sku : nestedObjSet) {
        Object value = filterFields(sku, sku.getClass(), classVsFieldMap);
        newSet.add(value);
      }
      return newSet;
    }

    List<String> requiredFields = classVsFieldMap.get(kclass.getSimpleName().toLowerCase());

    try {
      newObj = (SearchableEntity) kclass.newInstance();
      PropertyAccessor objectAccessor = PropertyAccessorFactory.forDirectFieldAccess(newObj);
      for (String fieldName : requiredFields) {
        Field classFieldObject = object.getClass().getDeclaredField(fieldName);
        classFieldObject.setAccessible(true);
        Object fieldValue = classFieldObject.get(object);

        if (classVsFieldMap.containsKey(fieldName) && null != fieldValue) {
          fieldValue = filterFields(fieldValue, fieldValue.getClass(), classVsFieldMap);
        }

        objectAccessor.setPropertyValue(fieldName, fieldValue);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return newObj;
  }

  /**
   * Find a single resource
   * 
   * @param fields
   * @param class1
   * @param isBroker
   * @param pageable
   * @param spec
   * @return
   */
  protected <T> T findOne(Integer id,
      Class klass,
      BaseRepository repository,
      Map<String, List<String>> classVsFieldMap) {

    T record = (T) repository.findOne(id);

    if (null != record && null != classVsFieldMap && !classVsFieldMap.isEmpty()) {
      return (T) filterFields(record, klass, classVsFieldMap);
    }

    return record;
  }

}
