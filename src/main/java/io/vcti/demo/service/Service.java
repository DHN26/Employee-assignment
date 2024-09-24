package io.vcti.demo.service;

import java.util.List;

import io.vcti.demo.entity.Employee;


public interface Service {
	public void addEmployee(Employee emp);
	public List<Employee> fetchAllEmployee();
	public Employee fetchById(int id);
	public void updateEmployee(int id, String name);
	public void deleteEmp(int id);

}
