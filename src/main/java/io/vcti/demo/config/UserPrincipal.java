package io.vcti.demo.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.vcti.demo.entity.Employee;

public class UserPrincipal implements UserDetails {

	private Employee emp;

	public UserPrincipal(Employee emp) {
		super();
		this.emp = emp;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + emp.getRole().toUpperCase()));
	    System.out.println("Authorities: " + authorities);
	    return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		System.out.println("Password fetched");
		return emp.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return emp.getUserName();
	}
	
	
}
