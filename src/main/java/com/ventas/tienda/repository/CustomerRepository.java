package com.ventas.tienda.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ventas.tienda.model.entities.Customer;

public interface CustomerRepository extends Repository<Customer>{
    Customer findByEmail(String email);
    Customer findByAddress(String address);
    Page<Customer> findByNameStartingWith(Pageable pageable, String name);
}
