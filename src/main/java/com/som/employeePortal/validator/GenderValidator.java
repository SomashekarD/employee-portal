package com.som.employeePortal.validator;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The Class GenderValidator.
 */
public class GenderValidator implements ConstraintValidator<Gender, String> {
	
	/** The genders. */
	List<String> genders = Arrays.asList("M", "F", "O");

	/**
	 * Checks if is valid.
	 *
	 * @param value the value
	 * @param context the context
	 * @return true, if is valid
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return genders.contains(value);
	}

}
