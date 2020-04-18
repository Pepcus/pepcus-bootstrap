package com.pepcus.apps.validators;

import static com.pepcus.apps.constant.ApplicationConstants.DATE_RANGE_SEPARATOR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

/**
 * Checks that a given date range is a valid date range.  
 * 
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, String>{
    
    private String datePattern; 
    
    @Override
    public boolean isValid(String dateRange, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(dateRange)) {
            return false;
        }
        String [] rangeArray = dateRange.split(DATE_RANGE_SEPARATOR);
        
        if (rangeArray == null || rangeArray.length < 2) {
            return false;
        }
        
        String startDateString = rangeArray[0].trim();
        String endDateString = rangeArray[1].trim();
        Date startDate = null;
        Date endDate = null;
        
        Date currentDate = Calendar.getInstance().getTime();
        
        try {
            startDate = new SimpleDateFormat(datePattern).parse(startDateString);
            endDate = new SimpleDateFormat(datePattern).parse(endDateString);
        } catch (ParseException e) {
            return false;
        }
        
        if (startDate.after(currentDate) || endDate.after(currentDate)) {
            return false;
        }
        
        if (endDate.before(startDate)) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.datePattern = constraintAnnotation.datePattern();
    }
    
}