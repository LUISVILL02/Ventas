package com.ventas.tienda.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ventas.tienda.model.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Customer findByEmail(String email);
    Customer findByAddress(String address);
    Page<Customer> findByNameLike(String name);
}
