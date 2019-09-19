package com.som.employeePortal;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.som.employeePortal.models.Employee;
import com.som.employeePortal.repositories.EmployeesRepository;

/**
 * The Class EmployeesController.
 */
@RestController
@RequestMapping("/employees")
@Validated
public class EmployeesController {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(EmployeesController.class);

	/** The repository. */
	@Autowired
	private EmployeesRepository repository;

	/**
	 * Gets the all employees.
	 *
	 * @return the all employees
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Employee> getAllEmployees(@RequestParam(defaultValue = "0", required = false) Integer pageId, @RequestParam(defaultValue = "10", required = false) Integer count) {
		return repository.findAll(PageRequest.of(pageId, count, Sort.by("firstName"))).getContent();
	}

	/**
	 * Gets the employee by id.
	 *
	 * @param id the id
	 * @return the employee by id
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Employee getEmployeeById(@PathVariable("id") ObjectId id) {
		Employee emp = repository.findBy_id(id);
		
		if(emp == null) {
			logger.warn("Couldn't find employee with Id: " + id.toHexString());
		}
		
		return emp;
	}

	/**
	 * Modify employee by id.
	 *
	 * @param id the id
	 * @param employee the employee
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void modifyEmployeeById(@PathVariable("id") ObjectId id, @Valid @RequestBody Employee employee) {
		employee.set_id(id);
		repository.save(employee);
		logger.info("modified employee with Id: " + employee.get_id());
	}

	/**
	 * Creates the employee.
	 *
	 * @param employee the employee
	 * @return the employee
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		employee.set_id(ObjectId.get());
		repository.save(employee);

		logger.info("Created new employee with Id: " + employee.get_id());
		return employee;
	}

	/**
	 * Delete employee.
	 *
	 * @param id the id
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteEmployee(@PathVariable ObjectId id) {
		Employee emp = repository.findBy_id(id);
		if(emp == null) {
			logger.warn("couldn't find employee with Id:" + id.toHexString());
			return;
		}
		repository.delete(emp);
		logger.info("deleted employee with Id:" + id.toHexString());
	}
}
