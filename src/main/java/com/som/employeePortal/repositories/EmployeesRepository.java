package com.som.employeePortal.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.som.employeePortal.models.Employee;

/**
 * The Interface EmployeesRepository.
 */
public interface EmployeesRepository extends MongoRepository<Employee, String>{
	
	/**
	 * Find by id.
	 *
	 * @param _id the id
	 * @return the employee
	 */
	Employee findBy_id(ObjectId _id);
}
