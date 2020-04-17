package com.pepcus.apps.api.validators;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
 
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
 
import javax.validation.Constraint;
import javax.validation.Payload;
 
/**
 *
 * The string has to be a valid and supported chart type.
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = com.pepcus.apps.api.validators.ValidChartTypeValidator.class)
@Documented
public @interface ValidChartType {
 
    String message() default "Not a valid chart type. Supported chart types are Bar, Area, Line, Pie, Doughnut and Column only";
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
 
}