package io.vcti.demo.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.vcti.demo.entity.Address;
import io.vcti.demo.entity.Employee;
import io.vcti.demo.service.EmployeeServiceImpl;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmpControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EmployeeServiceImpl service;

	private Employee employee;

	@BeforeEach
	void setUp() {
		employee = Employee.builder().id(444).name("jack").userName("jack@").password("123").role("admin")
				.build();
		employee.setPassword(new BCryptPasswordEncoder().encode("123"));
	}
	

	@Test
	void testSaveEmp() throws Exception {
	    
	    Address address = Address.builder().name("Main St").id(234).build();
	    employee.setAddress(address);  

	    // Send a POST request to save the employee
	    mockMvc.perform(post("/emp/save")
	            .contentType(MediaType.APPLICATION_JSON)
	            .with(httpBasic("eddie@", "123"))
	            .content(objectMapper.writeValueAsString(employee)))
	            .andExpect(status().isCreated())
	            .andExpect(content().string("Employee saved successfully."));

	}


	@Test
	void testFetchAll() throws Exception {
		List<Employee> employees = Arrays.asList(employee);
		System.out.println(employees);
		mockMvc.perform(get("/emp/fetch")).andExpect(status().isOk());
	}

	@Test
	void testFetchById() throws Exception {

		mockMvc.perform(get("/emp/fetch/{id}", 104)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("taylor")));
	}

	@Test
	void testUpdateEmp() throws Exception {

		mockMvc.perform(put("/emp/update/{id}", 253).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)).with(httpBasic("eddie@", "123"))) 																										
				.andExpect(status().isOk()).andExpect(content().string("Employee updated successfully."));
	}
	
	@Test
	void testDeleteEmp() throws Exception {

		 mockMvc.perform(delete("/emp/del/{id}", 253)
		            .with(httpBasic("eddie@", "123"))) 
		            .andExpect(status().isOk())
		            .andExpect(content().string("Deleted successfully."));
	}


}