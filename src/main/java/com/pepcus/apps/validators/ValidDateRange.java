package com.pepcus.apps.validators;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The string has to be a valid date range.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = com.pepcus.apps.validators.DateRangeValidator.class)
@Documented
public @interface ValidDateRange {

  String datePattern() default "MMM dd, yyyy";

  String message() default "Invalid date range. Please use valid date range.(e.g., Sep 01, 2018 - Sep 10, 2018)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
