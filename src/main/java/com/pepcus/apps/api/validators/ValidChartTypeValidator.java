package com.pepcus.apps.api.validators;

import static com.pepcus.apps.api.constant.ApplicationConstants.AREA_CHART;
import static com.pepcus.apps.api.constant.ApplicationConstants.BAR_CHART;
import static com.pepcus.apps.api.constant.ApplicationConstants.COLUMN_CHART;
import static com.pepcus.apps.api.constant.ApplicationConstants.DOUGHNUT_CHART;
import static com.pepcus.apps.api.constant.ApplicationConstants.LINE_CHART;
import static com.pepcus.apps.api.constant.ApplicationConstants.PIE_CHART;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

/**
 * Checks that a given type is a valid chart type supported by application. 
 *
 */
public class ValidChartTypeValidator implements ConstraintValidator<ValidChartType, String> {
 
    
    static List<String> supportedChartTypes = Arrays.asList(COLUMN_CHART,
                                                                    BAR_CHART, 
                                                                    PIE_CHART, 
                                                                    LINE_CHART, 
                                                                    AREA_CHART,
                                                                    DOUGHNUT_CHART);
 
    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(type) && !supportedChartTypes.contains(type.trim().toLowerCase())) {
            return false;
        }
        
        return true;
    }

    @Override
    public void initialize(ValidChartType constraintAnnotation) {
        // TODO Auto-generated method stub
        
    }}
