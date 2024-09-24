package io.vcti.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.vcti.demo.entity.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer>{

}
