package com.som.employeePortal.models;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.som.employeePortal.validator.DateOfBirth;
import com.som.employeePortal.validator.Gender;

/**
 * The Class Employee.
 */
@Document(collection="employees")
@TypeAlias("Employee")
public class Employee {
	
	/** The id. */
	@Id
	public ObjectId _id;
	
	/** The first name. */
	@Indexed(name="firstNameIdx", unique=false)
	@NotEmpty(message = "Employee First Name is compulsory")
	private String firstName;
	
	/** The last name. */
	private String lastName;
	
	/** The gender. */
	@Gender
	@NotEmpty(message = "Please provide employee gender")
	private String gender;
	
	/** The date of birth. */
	@DateOfBirth
	@NotNull(message = "Please provide date of birth in the format 'dd-MM-yyyy'")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date dateOfBirth;
	
	/** The department. */
	@NotEmpty(message = "Please provide department of employee")
	private String department;
	
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String get_id() {
		return _id.toHexString();
	}
	
	/**
	 * Sets the id.
	 *
	 * @param _id the new id
	 */
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	
	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * Sets the gender.
	 *
	 * @param gender the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	/**
	 * Gets the date of birth.
	 *
	 * @return the date of birth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	/**
	 * Sets the date of birth.
	 *
	 * @param dateOfBirth the new date of birth
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * Gets the department.
	 *
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	
	/**
	 * Sets the department.
	 *
	 * @param department the new department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
}
