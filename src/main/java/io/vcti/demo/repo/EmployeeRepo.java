package io.vcti.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.vcti.demo.entity.Address;
import io.vcti.demo.entity.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

	Optional<Address> findByName(Address address);

	Employee findByUserName(String username);

}
