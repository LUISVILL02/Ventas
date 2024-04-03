package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.Dtos.save.CustomerDtoSave;
import com.ventas.tienda.model.Dtos.send.CustomerDtoSend;
import com.ventas.tienda.model.entities.Customer;
import com.ventas.tienda.model.mapper.CustomerMapper;
import com.ventas.tienda.repository.CustomerRepository;
import com.ventas.tienda.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl extends ServiceImp<CustomerDtoSave, CustomerDtoSend, Customer> implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    protected CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        super(customerRepository, customerMapper);
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }


    @Override
    public Optional<CustomerDtoSend> findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return Optional.ofNullable(customerMapper.EntityToDtoSend(customer));
    }

    @Override
    public Optional<CustomerDtoSend> findByAddress(String address) {
        return Optional.ofNullable(customerMapper.EntityToDtoSend(customerRepository.findByAddress(address)));
    }

    @Override
    public Page<CustomerDtoSend> findByNameStartingWith(Pageable pageable, String name) {
        Pageable pageable1 = PageRequest.of(0, 10);
        Page<Customer> listCustomer = customerRepository.findByNameStartingWith(pageable1, name);
        return listCustomer.map(customerMapper::EntityToDtoSend);
    }

    @Override
    public CustomerDtoSend update(CustomerDtoSave customerDtoSave, Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            return customerMapper.EntityToDtoSend(
                    customerRepository.save(
                            customerMapper.dtoSaveToEntity(customerDtoSave)));
        }
        Customer customerUpdate = customer.get().updateCustomer(customerMapper.dtoSaveToEntity(customerDtoSave));
        return customerMapper.EntityToDtoSend(customerRepository.save(customerUpdate));
    }
}
