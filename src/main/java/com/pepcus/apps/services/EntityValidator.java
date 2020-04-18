package com.pepcus.apps.services;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * To validate an entity with their annotation based validators 
 * 
 *
 */
@Service
public class EntityValidator {
    
    private final Logger logger = LoggerFactory.getLogger(EntityValidator.class);
    
    /**
     * Validates given object for any constraint voilations and throws ConstraintViolationException if any 
     * voilations are found
     * 
     * @param entity
     */
    public <T> void validatePolicy(T entity) {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);

        boolean isConstraintFailed = false;
        if(constraintViolations!=null && !constraintViolations.isEmpty()) {
            isConstraintFailed = true;
            ConstraintViolationException ex = new ConstraintViolationException(constraintViolations);
            ex.getConstraintViolations().forEach(obj -> {
                String messageTemplate = obj.getConstraintDescriptor().getMessageTemplate();
                if (StringUtils.isNotBlank(messageTemplate)) {
                    logger.error("############ VALIDATION FAILED ##############");
                    logger.error("####### " + obj.getPropertyPath() + " " + obj.getMessage() + "###########");
                }
            });
            throw ex;
        }
    }

}
