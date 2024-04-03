package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.CustomerDtoSave;
import com.ventas.tienda.model.Dtos.send.CustomerDtoSend;
import com.ventas.tienda.model.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerService extends Service<CustomerDtoSave, CustomerDtoSend, Customer>{
    Optional<CustomerDtoSend> findByEmail(String email);
    Optional<CustomerDtoSend> findByAddress(String address);
    Page<CustomerDtoSend> findByNameStartingWith(Pageable pageable, String name);
    CustomerDtoSend update(CustomerDtoSave customerDtoSave, Long id);
}
