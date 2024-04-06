package com.ventas.tienda.repository;

import com.ventas.tienda.IntegrationDbRepositoryTest;
import com.ventas.tienda.model.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerRepositoryTest extends IntegrationDbRepositoryTest {

    private final CustomerRepository customerRepository;

    @Autowired
    CustomerRepositoryTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    void createCustomer() {
        Customer customer = Customer.builder()
                .name("Customer")
                .email("vill@gmail.com")
                .address("Calle 123")
                .build();

        customerRepository.save(customer);

        Customer customer2 = Customer.builder()
                .name("Customer2")
                .email("luis@gmail.com")
                .address("Calle 456")
                .build();
        customerRepository.save(customer2);

        customerRepository.flush();

    }

    @Test
    void givenCustomer_whenSave_thenCustomerId() {
        createCustomer();
        Customer customer = customerRepository.findAll().get(0);
        assertNotNull(customer.getIdCustomer());
    }

    @Test
    void givenEmail_whenFindByEmail_thenReturnCustomer() {
        createCustomer();

        List<Customer> customers = customerRepository.findAll();
        Customer customer = customerRepository.findByEmail("vill@gmail.com");

        assertNotNull(customer);
        assertEquals(customers.get(0).getEmail(), customer.getEmail());
    }
    @Test
    void givenAddress_whenFindByAddress_thenReturnCustomer() {
        createCustomer();

        List<Customer> customers = customerRepository.findAll();
        Customer customer = customerRepository.findByAddress("Calle 123");

        assertNotNull(customer);
        assertEquals(customers.get(0).getAddress(), customer.getAddress());
    }

    @Test
    void givenName_whenFindByNameStartingWith_thenReturnCustomer() {
        createCustomer();
        Pageable pageable = PageRequest.of(0, 10);

        List<Customer> customers = customerRepository.findAll();
        Customer customer = customerRepository.findByNameStartingWith(pageable,"Customer").getContent().get(0);

        assertNotNull(customer);
        assertEquals(customers.get(0).getName(), customer.getName());
    }

    @Test
    void givenName_whenFindByNameStartingWith_thenReturnEmpty() {
        createCustomer();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Customer> customer = customerRepository.findByNameStartingWith(pageable,"Customer3");

        assertThat(customer).isEmpty();
    }

    @Test
    void givenIdCustomer_whenFindByIdCustomer_thenReturnCustomer() {
        createCustomer();

        Long idCustomer = 1L;

        Customer customer = customerRepository.findById(idCustomer).get();

        assertEquals("Customer", customer.getName());
    }

    @Test
    void givenIdCustomer_whenDeleteById_thenCustomerDeleted() {
        createCustomer();

        Long idCustomer = 1L;

        customerRepository.deleteById(idCustomer);

        assertEquals(1, customerRepository.findAll().size());
    }
}