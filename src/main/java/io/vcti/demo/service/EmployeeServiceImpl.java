package io.vcti.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;

import io.vcti.demo.entity.Employee;
import io.vcti.demo.repo.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;

@org.springframework.stereotype.Service
@Transactional(rollbackFor = EntityNotFoundException.class, propagation = Propagation.REQUIRED)
public class EmployeeServiceImpl implements Service {

	@Autowired
	private EmployeeRepo empRepo;

	@Autowired
	private AddressServiceImpl addimpl;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JWTService jwtService;

	@Override
	public void addEmployee(Employee emp) {
		try {
			addimpl.addAddress(emp.getAddress());
		} catch (EntityNotFoundException e) {
			System.out.println("Address not saved, but proceeding with saving Employee");
		}
		empRepo.save(emp);
	}

	@Override
	public List<Employee> fetchAllEmployee() {
		return empRepo.findAll();
	}

	@Override
	public Employee fetchById(int id) {
		return empRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
	}

	@Override
	public void updateEmployee(int id, String name) {
		Employee emp = empRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
		emp.setName(name);
		empRepo.save(emp);
	}

	@Override
	public void deleteEmp(int id) {
		Employee emp = empRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
		empRepo.delete(emp);
	}

	public Employee verifyEmp(Employee e) throws Exception {
		int id = e.getId();
		Employee res = empRepo.findById(id).orElse(null);

		if (res == null) {
			throw new EntityNotFoundException("Employee not found with ID: " + id);
		}
		if (BCrypt.checkpw(e.getPassword(), res.getPassword()) && e.getUserName().equals(res.getUserName())) {
			return res;
		} else {
			System.out.println("Password or Username is incorrect.");
			throw new Exception("Bad user credentials!");
		}
	}

	public String verifyAuth(Employee e) throws Exception {
		Authentication auth=authManager.authenticate(
				new UsernamePasswordAuthenticationToken(e.getUserName(), e.getPassword())
				);
		if(auth.isAuthenticated()==true)
			return jwtService.generateToken(e.getUserName());
		else throw new Exception("Bad credentials!");			
		
	}

}
