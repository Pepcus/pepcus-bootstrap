package com.pepcus.apps.api.utils;

import static com.pepcus.apps.api.constant.ApplicationConstants.ASCENDING;
import static com.pepcus.apps.api.constant.ApplicationConstants.DATE_PATTERN;
import static com.pepcus.apps.api.constant.ApplicationConstants.DEFAULT_LIMIT;
import static com.pepcus.apps.api.constant.ApplicationConstants.DEFAULT_OFFSET;
import static com.pepcus.apps.api.constant.ApplicationConstants.DESENDING;
import static com.pepcus.apps.api.constant.ApplicationConstants.EQUALS_OPERATOR;
import static com.pepcus.apps.api.constant.ApplicationConstants.GREATER_THAN;
import static com.pepcus.apps.api.constant.ApplicationConstants.GREATER_THAN_EQUALS;
import static com.pepcus.apps.api.constant.ApplicationConstants.LESS_THAN;
import static com.pepcus.apps.api.constant.ApplicationConstants.LESS_THAN_EQUALS;
import static com.pepcus.apps.api.constant.ApplicationConstants.LIST_OPERATORS;
import static com.pepcus.apps.api.constant.ApplicationConstants.OPERATOR_BASED_ATTRIBUTES;
import static com.pepcus.apps.api.constant.ApplicationConstants.OPERATOR_START;
import static com.pepcus.apps.api.constant.ApplicationConstants.SEARCH_SPEC;
import static com.pepcus.apps.api.constant.ApplicationConstants.VALID_ISO_8601_DATE_DISPLAY_FORMAT;
import static com.pepcus.apps.api.constant.ApplicationConstants.VALID_ISO_8601_DATE_FORMAT;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.apps.api.constant.ApplicationConstants;
import com.pepcus.apps.api.db.entities.SearchableEntity;
import com.pepcus.apps.api.exception.APIErrorCodes;
import com.pepcus.apps.api.exception.ApplicationException;
import com.pepcus.apps.api.services.EntitySearchSpecification;
import com.pepcus.apps.api.services.OffsetPageRequest;

/**
 * Class is specific to keep utility methods for Entity Search feature. 
 *
 */
public class EntitySearchUtil {

    private static Logger logger = LoggerFactory.getLogger(EntitySearchUtil.class);

    /**
     * Get Pageable instance
     * @param offset
     * @param limit
     * @param sortedBy
     * @return
     */
    public static Pageable getPageable(Integer offset, Integer limit, String sortedBy, String defaultSortedBy) {

        OffsetPageRequest pageable = null;

        offset = offset == null ? DEFAULT_OFFSET : offset;
        limit = limit == null ? DEFAULT_LIMIT : limit;

        Sort sort = getSort(sortedBy, defaultSortedBy);

        pageable = new OffsetPageRequest(offset/limit, limit, sort);

        pageable.setOffset(offset);

        return pageable;
    }
    
    public static Sort getSort(String sortedBy, String defaultSortedBy) {
        sortedBy = StringUtils.isBlank(sortedBy) ? defaultSortedBy : sortedBy.trim();
        Sort.Direction sortDirection = getSortDirection(sortedBy);
        
        sortedBy = extractSortDirection(sortedBy, sortDirection); // Extracted out + or - character from sortBy string
        return new Sort(sortDirection, sortedBy);
    }

    /**
     * It will extract + or - character those stands for sort direction from column field name
     * @param sortedBy
     * @param sortDirection
     * @return
     */
    public static String extractSortDirection(String sortedBy, Sort.Direction sortDirection) {
        String directionIndicator = sortedBy.substring(0,1);
        if(directionIndicator.equals(ASCENDING) || directionIndicator.equals(DESENDING) ) {
            sortedBy = sortedBy.substring(1);
        } 

        return sortedBy.trim();
    }

    /**
     * Return formatted string to display like +-fieldName will be formatted as 
     * 
     * fieldName ASC or fieldName DESC
     */
    public static String getFormattedString(String sortBy) {
        if (StringUtils.isBlank(sortBy)) {
            return sortBy;
        }
        Sort.Direction sortDirection = getSortDirection(sortBy);
        String sortedBy = extractSortDirection(sortBy, sortDirection);

        return sortedBy + " " + sortDirection.name();
    }

    /**
     * Get the first character out from sortedBy value. 
     * like +companyName
     * @param sortedBy
     * @return
     */
    public static Direction getSortDirection(String sortedBy) {
        String sortDirection =  sortedBy.substring(0, 1);
        return DESENDING.equalsIgnoreCase(sortDirection) ? Direction.DESC : Direction.ASC;
    }

    /**
     * @return
     */
    public static Set<String> getSortAndLimitRequestParams() {
        //TODO: To externalize them
        String requestParams[] = {"offset", "limit", "sort", "fields"};
        return new HashSet<String>(Arrays.asList(requestParams));
    }

    /**
     * To validate given Class has field with fieldName or not
     * @param <T>
     * 
     * @param kclass
     * @param fieldName
     * @return
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     */
    public static <T> boolean classHasField(Class<T> kclass, String fieldName) {
        
        if(fieldName.contains(OPERATOR_START)) {
            fieldName = fieldName.substring(0, fieldName.indexOf(OPERATOR_START));
            if (!OPERATOR_BASED_ATTRIBUTES.contains(fieldName)) {
                throw ApplicationException.createBadRequest(APIErrorCodes.INVALID_OPERATOR_ATTRIBUTE, fieldName, StringUtils.join(OPERATOR_BASED_ATTRIBUTES.toArray()));
            }
        }
        
        try {
            Field field = kclass.getDeclaredField(fieldName);
            if (field == null) {
                return false;
            }
        } catch(NoSuchFieldException | SecurityException ex) {
            return false;
        }

        return true;
    }

    /**
     * To check type of field for given parameters and validate it is java.lang.String or not
     * 
     * @param <T>
     * @param kclass
     * @param fieldName
     * @param fieldType
     * @return
     */
    public static <T> boolean isFieldOfType(Class<T> kclass, String fieldName,  Class<?> fieldType) {
        Field field = null;
        try {
            field = kclass.getDeclaredField(fieldName);
        } catch(NoSuchFieldException | SecurityException ex) {
            return false;
        }
        if (field.getType().isAssignableFrom(fieldType)) {
            return true;
        }

        return false;
    }

    /**
     * To check type of field for given parameters and validate it is java.lang.String or not
     * 
     * @param <T>
     * @param kclass
     * @param fieldName
     * @return
     */
    public static <T> boolean isStringField(Class<T> kclass, String fieldName) {
        return isFieldOfType(kclass, fieldName, String.class);
    }

    /**
     * To check type of field for given parameters and validate it is java.lang.String or not
     * 
     * @param <T>
     * @param kclass
     * @param fieldName
     * @return
     */
    public static <T> boolean isUserCompanyField(Class<T> kclass, String fieldName) {
        if ("brokerId".equalsIgnoreCase(fieldName) ||
                "brokerName".equalsIgnoreCase(fieldName) ||
                "companyName".equalsIgnoreCase(fieldName)||
                "companyId".equalsIgnoreCase(fieldName)) {
            return true;
        }
        return false;
    }

    /**
     * To check type of field for given parameters and validate it is java.util.Date or not
     * 
     * @param <T>
     * @param kclass
     * @param fieldName
     * @return
     */
    public static <T> boolean isDateField(Class<T> kclass, String fieldName) {
        return isFieldOfType(kclass, fieldName, Date.class);
    }

    /**
     * Get the date for a given string
     * @param dateStr
     * @param key
     * @return
     * @throws ApplicationException 
     */
    public static Date convertToDate(String value, String key, boolean includeTime) throws ApplicationException {

        if (includeTime) {
            value = value.replace(" ", "+");
        }
        
        SimpleDateFormat sdf = includeTime ? new SimpleDateFormat(VALID_ISO_8601_DATE_FORMAT) : new SimpleDateFormat(DATE_PATTERN);
        sdf.setLenient(false);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            throw ApplicationException.createBadRequest(APIErrorCodes.INVALID_DATE_FORMAT, key, includeTime ? VALID_ISO_8601_DATE_DISPLAY_FORMAT : DATE_PATTERN);
        } catch (Exception e) {
            throw ApplicationException.createBadRequest(APIErrorCodes.INVALID_DATE_FORMAT, key, includeTime ? VALID_ISO_8601_DATE_DISPLAY_FORMAT : DATE_PATTERN);
        }
    }

    /**
     * To filter request parameters on field Name
     * @param <T>
     * 
     * @param allRequestParams
     * @param kclass
     * @return 
     */
    public static <T> Map<String, String> extractParametersForFilterRecords(Map<String, String> allRequestParams,
            Class<T> kclass) throws ApplicationException {

        Set<String> excludedParams = getSortAndLimitRequestParams();

        Map<String, String> filteredParameters = new HashMap<String, String>();

        for(Entry<String, String> entry : allRequestParams.entrySet()) { 
            if (excludedParams.contains(entry.getKey())) {
                continue;
            } else if(entry.getKey().equals(SEARCH_SPEC)) {
                filteredParameters.put(entry.getKey(), entry.getValue());
            } else if (!classHasField(kclass, entry.getKey())) {
                throw ApplicationException.createBadRequest(APIErrorCodes.REQUEST_PARAM_INVALID, entry.getKey(), kclass.getName());
            } 
            filteredParameters.put(entry.getKey(), entry.getValue());
        }

        return filteredParameters;
    }

    /**
     * Create Entity Search Specification
     * It will give priority over requestParameters on searchSpec
     * 
     * @param searchSpec
     * @param requestParameters
     * @return
     * @throws ApplicationException 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Specification<T> getEntitySearchSpecification(String searchSpec,
            Map<String, String> requestParameters, Class<T> kclass, SearchableEntity entity) throws ApplicationException {

        //To get any other requestParameter like Company's fieldName to filter record on filterName
        if (requestParameters != null) {
            Map<String, String> requestParametersForFilterRecords = extractParametersForFilterRecords(requestParameters, kclass);
            if (requestParametersForFilterRecords != null && !requestParametersForFilterRecords.isEmpty()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Applying filter on records ");
                }
                return new EntitySearchSpecification(requestParametersForFilterRecords, entity);
            } 
        }

        return new EntitySearchSpecification(entity);
    }

    /**
     * @param searchParam
     * @param filterPredicate
     * @param criteriaBuilder
     * @param searchKeyPath
     */
    public static void addOperatorExpression(Entry<String, String> searchParam, Predicate filterPredicate, CriteriaBuilder criteriaBuilder, Path<String> searchKeyPath) {
        
        boolean operatorValid = false;
        
        for (String operator : ApplicationConstants.LIST_OPERATORS) {
            if(searchParam.getKey().contains(operator)) {
                operatorValid = true;
                Date date = convertToDate(searchParam.getValue(), searchParam.getKey(), true);
                Integer dateInMs = (int) (date.getTime()/1000L);
                setOperatorInPredicate(filterPredicate, searchKeyPath, dateInMs, operator, criteriaBuilder);
                break;
            }
        }
        
        if (!operatorValid) {
            throw ApplicationException.createBadRequest(APIErrorCodes.INVALID_OPERATOR, StringUtils.join(LIST_OPERATORS.toArray()));
        }
    }

    /**
     * @param filterPredicate
     * @param searchKeyPath
     * @param dateInMs
     * @param operator 
     * @param criteriaBuilder 
     */
    private static void setOperatorInPredicate(Predicate filterPredicate,
            Path<String> searchKeyPath, Integer dateInMs, String operator, CriteriaBuilder criteriaBuilder) {

        switch (operator) {
        case LESS_THAN:
            filterPredicate.getExpressions().add(criteriaBuilder.lessThan(searchKeyPath, dateInMs.toString()));
            break;
        case GREATER_THAN:
            filterPredicate.getExpressions().add(criteriaBuilder.greaterThan(searchKeyPath, dateInMs.toString()));
            break;
        case LESS_THAN_EQUALS:
            filterPredicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(searchKeyPath, dateInMs.toString()));
            break;
        case GREATER_THAN_EQUALS:
            filterPredicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(searchKeyPath, dateInMs.toString()));
            break;
        case EQUALS_OPERATOR:
            filterPredicate.getExpressions().add(criteriaBuilder.equal(searchKeyPath, dateInMs.toString()));
            break;
        default:
        }
        
    }
}
