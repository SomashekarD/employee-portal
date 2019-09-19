package com.som.employeePortal.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.som.employeePortal.models.Employee;
import com.som.employeePortal.repositories.EmployeesRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class EmployeesControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private EmployeesRepository mockRepository;

	@Test
	public void addEmployee() throws JSONException {

		String employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"firstName\":\"Deepash\",\"lastName\":\"Rao\",\"gender\":\"M\",\"dateOfBirth\":\"01-05-1983\",\"department\":\"Finance\"}";

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(1)).save(any(Employee.class));
	}

	@Test
	public void postEmptyJSON() throws JSONException {

		String employeeInJson = "{}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"status\":400,\"errors\":[\"Please provide department of employee\","
				+ "\"Gender value not allowed. It should be 'M' for Male, 'F' for Female and 'O' for others\","
				+ "\"Employee date of birth should be between 18yrs and 60yrs older from current date\","
				+ "\"Employee First Name is compulsory\","
				+ "\"Please provide date of birth in the format 'dd-MM-yyyy'\","
				+ "\"Please provide employee gender\"]}";

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(0)).save(any(Employee.class));
	}

	@Test
	public void postEmptyBody() throws JSONException {

		String employeeInJson = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		verify(mockRepository, times(0)).save(any(Employee.class));
	}
	
	@Test
	public void addEmployeeWithEmptyFirstName() throws JSONException {

		String employeeInJson = "{\"firstName\":\"\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"status\":400,\"errors\":[\"Employee First Name is compulsory\"]}";
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(0)).save(any(Employee.class));
	}
	
	@Test
	public void addEmployeeWithInCorrectGender() throws JSONException {

		String employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"X\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"status\":400,\"errors\":[\"Gender value not allowed. It should be 'M' for Male, 'F' for Female and 'O' for others\"]}";
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(0)).save(any(Employee.class));
	}
	
	@Test
	public void addEmployeeWithDOBNotInRange() throws JSONException {

		String employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1900\", \"department\":\"Finance\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"status\":400,\"errors\":[\"Employee date of birth should be between 18yrs and 60yrs older from current date\"]}";
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(0)).save(any(Employee.class));
	}

	@Test
	public void addEmployeeWithEmptyDepartment() throws JSONException {

		String employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"status\":400,\"errors\":[\"Please provide department of employee\"]}";
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(0)).save(any(Employee.class));
	}
	
	@Test
	public void modifyEmployee() throws JSONException, JsonParseException, JsonMappingException, IOException {

		String employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"firstName\":\"Deepash\",\"lastName\":\"Rao\",\"gender\":\"M\",\"dateOfBirth\":\"01-05-1983\",\"department\":\"Finance\"}";

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(1)).save(any(Employee.class));
		
		Employee emp = new ObjectMapper().readValue(response.getBody(), Employee.class);
		
		employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"HR\"}";
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		entity = new HttpEntity<>(employeeInJson, headers);

		response = restTemplate.exchange("/employees/"+emp.get_id(), HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(mockRepository, times(2)).save(any(Employee.class));
	}
	
	@Test
	public void deleteEmployee() throws JSONException, JsonParseException, JsonMappingException, IOException {

		String employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"firstName\":\"Deepash\",\"lastName\":\"Rao\",\"gender\":\"M\",\"dateOfBirth\":\"01-05-1983\",\"department\":\"Finance\"}";

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(1)).save(any(Employee.class));
		
		Employee emp = new ObjectMapper().readValue(response.getBody(), Employee.class);
		
		employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"HR\"}";
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		entity = new HttpEntity<>(employeeInJson, headers);
		
		when(mockRepository.findBy_id(new ObjectId(emp.get_id()))).thenReturn(emp);
		doNothing().when(mockRepository).delete(emp);

		response = restTemplate.exchange("/employees/"+emp.get_id(), HttpMethod.DELETE, null, String.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(mockRepository, times(1)).findBy_id(new ObjectId(emp.get_id()));
		verify(mockRepository, times(1)).delete(any(Employee.class));
	}
	
	@Test
	public void getEmployeeById() throws JSONException, JsonParseException, JsonMappingException, IOException {

		String employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		String expectedJson = "{\"firstName\":\"Deepash\",\"lastName\":\"Rao\",\"gender\":\"M\",\"dateOfBirth\":\"01-05-1983\",\"department\":\"Finance\"}";

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(1)).save(any(Employee.class));
		
		Employee emp = new ObjectMapper().readValue(response.getBody(), Employee.class);
		
		employeeInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"HR\"}";
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		entity = new HttpEntity<>(employeeInJson, headers);
		
		when(mockRepository.findBy_id(new ObjectId(emp.get_id()))).thenReturn(emp);

		response = restTemplate.exchange("/employees/"+emp.get_id(), HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(mockRepository, times(1)).findBy_id(new ObjectId(emp.get_id()));
	}
	
	@Test
	public void getAllEmployeesSortedByFirstName() throws JSONException, JsonParseException, JsonMappingException, IOException {

		String employeeFirstInJson = "{\"firstName\":\"Sujan\", \"lastName\":\"Kumar\", \"gender\":\"M\", \"dateOfBirth\":\"01-07-1985\", \"department\":\"HR\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(employeeFirstInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/employees/", entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(employeeFirstInJson, response.getBody(), false);

		Employee empOne = new ObjectMapper().readValue(response.getBody(), Employee.class);
		
		String employeeSecondInJson = "{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"}";
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		entity = new HttpEntity<>(employeeSecondInJson, headers);
		
		response = restTemplate.postForEntity("/employees/", entity, String.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(employeeSecondInJson, response.getBody(), false);
		
		Employee empSecond = new ObjectMapper().readValue(response.getBody(), Employee.class);

		verify(mockRepository, times(2)).save(any(Employee.class));

		List<Employee> employeeList = new ArrayList<Employee>();
		employeeList.add(empOne);
		employeeList.add(empSecond);
		
		Page<Employee> empLoyeePages = new PageImpl<>(employeeList);
		
		when(mockRepository.findAll(PageRequest.of(0, 10, Sort.by("firstName")))).thenReturn(empLoyeePages);
		response = restTemplate.exchange("/employees/", HttpMethod.GET, null, String.class);
		
		String expectedJson = "[{\"firstName\":\"Deepash\", \"lastName\":\"Rao\", \"gender\":\"M\", \"dateOfBirth\":\"01-05-1983\", \"department\":\"Finance\"},"
				+ "{\"firstName\":\"Sujan\", \"lastName\":\"Kumar\", \"gender\":\"M\", \"dateOfBirth\":\"01-07-1985\", \"department\":\"HR\"}]";
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);
		verify(mockRepository, times(1)).findAll(PageRequest.of(0, 10, Sort.by("firstName")));
	}
}
