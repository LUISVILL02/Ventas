package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.CustomerDtoSave;
import com.ventas.tienda.model.Dtos.send.CustomerDtoSend;
import com.ventas.tienda.model.entities.Customer;
import com.ventas.tienda.model.entities.Product;
import com.ventas.tienda.model.mapper.CustomerMapper;
import com.ventas.tienda.repository.CustomerRepository;
import com.ventas.tienda.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    Customer customer;
    CustomerDtoSend customerDtoSend;
    CustomerDtoSave customerDtoSave;

    private Page<Customer> createPageWithSingleProduct(Customer customer) {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        return new PageImpl<>(customerList);
    }

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .idCustomer(1L)
                .name("Juan")
                .email("villamilluis123@gmail.com")
                .address("Calle 123")
                .orders(new ArrayList<>())
                .build();
        customerDtoSend = CustomerDtoSend.builder()
                .idCustomer(1L)
                .name("Juan")
                .email("villamilluis123@gmail.com")
                .address("Calle 123")
                .orders(new ArrayList<>())
                .build();

        customerDtoSave = CustomerDtoSave.builder()
                .name("Juan")
                .email("villamilluis123²gmail.com")
                .address("Calle 123")
                .build();
    }

    @Test
    void givenEmail_whenFindByEmail_thenReturnCustomerDtoSend() {
        String email = "villamilluis123@gmail.com";
        when(customerRepository.findByEmail(email)).thenReturn(customer);
        when(customerMapper.EntityToDtoSend(customer)).thenReturn(customerDtoSend);

        Optional<CustomerDtoSend> customerDtoSendOptional = customerService.findByEmail(email);

        assertTrue(customerDtoSendOptional.isPresent());
        assertEquals(customerDtoSend, customerDtoSendOptional.get());
        assertEquals(customerDtoSendOptional.get().getEmail(), customer.getEmail());
    }

    @Test
    void givenAddress_whenFindByAddress_thenReturnCustomerDtoSend() {
        String address = "Calle 123";
        when(customerRepository.findByAddress(address)).thenReturn(customer);
        when(customerMapper.EntityToDtoSend(customer)).thenReturn(customerDtoSend);

        Optional<CustomerDtoSend> customerDtoSendOptional = customerService.findByAddress(address);

        assertTrue(customerDtoSendOptional.isPresent());
        assertEquals(customerDtoSend, customerDtoSendOptional.get());
        assertEquals(customerDtoSendOptional.get().getAddress(), customer.getAddress());
    }
    @Test
    void givenName_whenFindByNameStartingWith_thenReturnPageCustomerDtoSend() {
        String name = "Juan";
        Page<Customer> page = createPageWithSingleProduct(customer);

        when(customerMapper.EntityToDtoSend(page.getContent().get(0))).thenReturn(customerDtoSend);

        when(customerRepository.findByNameStartingWith(PageRequest.of(0, 10), name)).thenReturn(page);

        Page<CustomerDtoSend> result = customerService.findByNameStartingWith(PageRequest.of(0, 10), name);
        assertNotNull(result);
        assertEquals(result.getContent().get(0).getName(), customerDtoSend.getName());
    }

    @Test
    void givenCustomerDtoSave_whenSave_thenReturnCustomerDtoSend() {
        when(customerMapper.dtoSaveToEntity(customerDtoSave)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.EntityToDtoSend(customer)).thenReturn(customerDtoSend);

        CustomerDtoSend result = customerService.save(customerDtoSave);
        assertNotNull(result.getIdCustomer());
        assertEquals(result.getName(), customer.getName());
    }

}