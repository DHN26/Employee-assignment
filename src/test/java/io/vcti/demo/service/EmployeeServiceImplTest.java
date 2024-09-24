package io.vcti.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

import io.vcti.demo.entity.Address;
import io.vcti.demo.entity.Employee;
import io.vcti.demo.repo.AddressRepo;
import io.vcti.demo.repo.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class EmployeeServiceImplTest {

	@Mock
	private AddressRepo addRepo;

	@Mock
	private EmployeeRepo empRepo;

	@Mock
	private AddressServiceImpl addressService;

	@InjectMocks
	private EmployeeServiceImpl empService;



	@Test
	public void addEmployeeSuccessAddressSaved() {
		Address address = Address.builder().name("Main Street").build();

		Employee emp = Employee.builder().name("poo").password("poo@").role("user").address(address) // Set the address
																										// field
				.build();	
		doNothing().when(addressService).addAddress(emp.getAddress());		
		empService.addEmployee(emp);		
		verify(addressService).addAddress(emp.getAddress()); 
		verify(empRepo).save(emp); 
	}

	@Test
	public void addEmployeeFailureAddressNotSaved() {
		Address address = Address.builder().name("Main Street").build();

		Employee emp = Employee.builder().name("poo").password("poo@").role("user").address(address) // Set the address
																										// field
				.build();

		doThrow(new EntityNotFoundException("Address not found")).when(addressService).addAddress(emp.getAddress());
		empService.addEmployee(emp);
		verify(addressService).addAddress(emp.getAddress()); 
		verify(empRepo).save(emp); 
	}

	@Test
	@DisplayName("fetch All TestMethod")
	public void fetchAllEmployeeTest() {
		Employee emp1 = Employee.builder().id(104).name("allen").userName("allen@").role("admin").build();
		Mockito.when(empRepo.findAll()).thenReturn(List.of(emp1));
		List<Employee> listFound = empService.fetchAllEmployee();
		assertEquals(List.of(emp1), listFound);
	}

	@Test
	public void fetchByIdSuccess() {
		Employee emp1 = Employee.builder().id(104).name("allen").userName("allen@").role("admin").build();
		Mockito.when(empRepo.findById(104)).thenReturn(Optional.of(emp1));
		Employee empFound = empService.fetchById(104);
		assertEquals(empFound, emp1);
	}

	@Test
	public void fetchByIdFailure() {
		int id = 2003;
		Mockito.when(empRepo.findById(id)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> {
			empService.fetchById(id);
		});
	}

	@Test
	public void updateNameSuccess() {
		int id = 104;
		String newName = "alexander";
		Employee existingEmp = Employee.builder().id(104).name("alex").password("alex@").role("admin").build();
		Employee updatedEmp = Employee.builder().id(104).name(newName).password("alex@").role("admin").build();

		when(empRepo.findById(104)).thenReturn(Optional.of(existingEmp));
		when(empRepo.save(updatedEmp)).thenReturn(updatedEmp);

		empService.updateEmployee(104, newName);

		verify(empRepo).findById(id);
		verify(empRepo).save(argThat(employee -> employee.getName().equals(newName) && employee.getId() == id));
	}

	@Test
	public void updateNameFailure() {
		int id = 2003;
		String newName = "allen";

		when(empRepo.findById(id)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> {
			empService.updateEmployee(id, newName);
		});
	}

	@Test
	public void deleteEmpSuccess() {
		int id = 103;
		Employee existingEmp = Employee.builder().id(103).name("alen").password("alen@").role("admin").build();
		when(empRepo.findById(id)).thenReturn(Optional.of(existingEmp));
		empService.deleteEmp(id);
		verify(empRepo).delete(existingEmp);
	}

	@Test
	public void deleteEmpFailure()
	{
		int id = 103;
		when(empRepo.findById(id)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> {
            empService.deleteEmp(id);
        });
	}
}
