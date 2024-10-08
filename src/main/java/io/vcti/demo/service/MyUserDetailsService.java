package io.vcti.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.vcti.demo.config.UserPrincipal;
import io.vcti.demo.entity.Employee;
import io.vcti.demo.repo.EmployeeRepo;


@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private EmployeeRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee emp=repo.findByUserName(username);
		if(emp==null)
			throw new UsernameNotFoundException("User not found!");
		return new UserPrincipal(emp);
	}

}
