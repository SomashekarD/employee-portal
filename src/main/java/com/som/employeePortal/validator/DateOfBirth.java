package com.som.employeePortal.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The Interface DateOfBirth.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DateOfBirthValidator.class)
@Documented
public @interface DateOfBirth {
	
	/**
	 * Message.
	 *
	 * @return the string
	 */
	String message() default "Employee date of birth should be between 18yrs and 60yrs older from current date";

    /**
     * Groups.
     *
     * @return the class[]
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     *
     * @return the class<? extends payload>[]
     */
    Class<? extends Payload>[] payload() default {};
}
