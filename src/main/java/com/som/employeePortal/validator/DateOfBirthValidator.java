package com.som.employeePortal.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The Class DateOfBirthValidator.
 */
public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, Date> {
	

	/**
	 * Checks if is valid.
	 *
	 * @param value the value
	 * @param context the context
	 * @return true, if is valid
	 */
	@Override
	public boolean isValid(Date value, ConstraintValidatorContext context) {
		
		if(value == null)
			return false;
		
		LocalDate employeeDOB = value.toInstant().atZone(ZoneId.systemDefault())
			      .toLocalDate();
		LocalDate now = LocalDate.now();
		
		LocalDate nowMinus18 = now.minusYears(18);
		
		LocalDate nowMinus60 = now.minusYears(60);
		
		return (employeeDOB.isBefore(nowMinus18) && employeeDOB.isAfter(nowMinus60)) 
				|| (employeeDOB.isEqual(nowMinus18) || employeeDOB.isEqual(nowMinus60));
	}

}
