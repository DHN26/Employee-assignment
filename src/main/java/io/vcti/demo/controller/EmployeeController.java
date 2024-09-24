package io.vcti.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.vcti.demo.entity.Employee;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
	List<Employee> list = new ArrayList<>();
	int id = 1;

	@PostMapping("/add")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee e) {
		e.setId(id++);
		list.add(e);
		return ResponseEntity.status(HttpStatus.CREATED).body(e);
	}

	@GetMapping("/read")
	public ResponseEntity<List<Employee>> readAllEmployee() {
		return ResponseEntity.ok(list);
	}

	@GetMapping("/read/{id}")
	public ResponseEntity<?> readById(@PathVariable int id) {
		for (Employee e : list) {
			if (e.getId() == id)
				return ResponseEntity.ok(e);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!");

	}

	@PutMapping("/updateEmployee/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestBody String newName) {
		for (Employee e : list) {
			if (e.getId() == id) {
				e.setName(newName);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated successfully!");

			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!");
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
		for (Employee e : list) {
			if (e.getId() == id) 
			{
				list.remove(e);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted successfully!");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!");

	}

}
