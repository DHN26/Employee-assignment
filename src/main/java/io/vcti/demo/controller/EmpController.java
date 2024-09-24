package io.vcti.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.vcti.demo.entity.Employee;
import io.vcti.demo.service.EmployeeServiceImpl;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/emp")
public class EmpController {

	@Autowired
	private EmployeeServiceImpl service;

	BCryptPasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@PostMapping("/save")
	public ResponseEntity<String> saveEmp(@RequestBody Employee e) {
		String pwd=e.getPassword();
		String newPwd=encoder().encode(pwd);
		e.setPassword(newPwd);
		service.addEmployee(e);
		return ResponseEntity.status(HttpStatus.CREATED).body("Employee saved successfully.");
	}

	@GetMapping("/fetch")
	public ResponseEntity<List<Employee>> fetchAll() {
		List<Employee> employees = service.fetchAllEmployee();

		if (employees.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(employees);
		}
	}

	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> fetchById(@PathVariable int id) {
		Employee employee = service.fetchById(id);
		return ResponseEntity.ok(employee);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateEmp(@PathVariable int id, @RequestBody Employee emp) {
		service.updateEmployee(id, emp.getName());
		return ResponseEntity.ok("Employee updated successfully.");
	}

	@DeleteMapping("/del/{id}")
	public ResponseEntity<String> deleteEmp(@PathVariable int id) {
		service.deleteEmp(id);
		return ResponseEntity.ok("Deleted successfully.");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Employee e) throws Exception
	{
		//Employee res=service.verifyEmp(e);
		String val=service.verifyAuth(e);
		return ResponseEntity.status(HttpStatus.OK).body("Login successful with given credentials. And token is : "+val);
	}
	
	
//	@ExceptionHandler({ EntityNotFoundException.class })//no need to specify
//	public ResponseEntity<?> exceptionMethod(EntityNotFoundException e) {
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//	}

}
