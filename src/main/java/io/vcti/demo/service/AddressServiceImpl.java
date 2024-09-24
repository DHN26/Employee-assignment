package io.vcti.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.vcti.demo.entity.Address;
import io.vcti.demo.repo.AddressRepo;
import jakarta.persistence.EntityNotFoundException;

@Service

public class AddressServiceImpl implements AddressService{
	
	@Autowired
	AddressRepo adrepo;

	@Override
	@Transactional(rollbackFor = EntityNotFoundException.class, propagation = Propagation.REQUIRED)
	public void addAddress(Address a) {
	    if (a.getName() != null && !a.getName().isEmpty()) {
	        adrepo.save(a);
	    } else {
	        throw new EntityNotFoundException("Address name is null or empty");
	    }
	}

}
