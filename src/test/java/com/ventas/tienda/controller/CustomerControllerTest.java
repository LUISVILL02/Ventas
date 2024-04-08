package com.ventas.tienda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ventas.tienda.model.Dtos.save.CustomerDtoSave;
import com.ventas.tienda.model.Dtos.send.CustomerDtoSend;
import com.ventas.tienda.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    private static final String URL = "/api/v1/customers";
    private static final String CONTENT_TYPE = "application/json";

    CustomerDtoSave customerDtoSave;
    CustomerDtoSend customerDtoSend;

    @BeforeEach
    void setUp() {
        customerDtoSave = CustomerDtoSave.builder()
                .name("Customer")
                .email("villa@Gmail.com")
                .address("Calle 123")
                .build();
        customerDtoSend = CustomerDtoSend.builder()
                .idCustomer(1L)
                .name("Customer")
                .email("villa@Gmail.com")
                .address("Calle 123")
                .orders(new ArrayList<>())
                .build();

    }

    @Test
    void getById() throws Exception {
        Long idCustomer = 1L;
        when(customerService.findById(idCustomer)).thenReturn(java.util.Optional.of(customerDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/" + idCustomer)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCustomer").value(customerDtoSend.getIdCustomer()))
                .andExpect(jsonPath("$.name").value(customerDtoSend.getName()))
                .andExpect(jsonPath("$.email").value(customerDtoSend.getEmail()))
                .andExpect(jsonPath("$.address").value(customerDtoSend.getAddress()))
                .andExpect(jsonPath("$.orders").isEmpty());
    }
    @Test
    void getByIdNotFound() throws Exception {
        long idCustomer = 100L;

        when(customerService.findById(idCustomer)).thenThrow(new RuntimeException("Customer not found"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/" + idCustomer)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Customer not found"));
    }

    @Test
    void getAll() throws Exception {
        Page<CustomerDtoSend> customerDtoSendPage = createPageWithSingleProduct(customerDtoSend);

        when(customerService.findAll()).thenReturn(customerDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getByEmail() throws Exception {
        String email = "villa@Gmail.com";

        when(customerService.findByEmail(email)).thenReturn(java.util.Optional.of(customerDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/email/" + email)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCustomer").value(customerDtoSend.getIdCustomer()))
                .andExpect(jsonPath("$.name").value(customerDtoSend.getName()))
                .andExpect(jsonPath("$.email").value(customerDtoSend.getEmail()))
                .andExpect(jsonPath("$.address").value(customerDtoSend.getAddress()))
                .andExpect(jsonPath("$.orders").isEmpty());
    }
    @Test
    void getByEmailNotFound() throws Exception {
        String email = "villa@Gmail.com";

        when(customerService.findByEmail(email)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/email/" + email)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Customer not found"));
    }

    @Test
    void getByAddress() throws Exception {
        String address = "address 123";

        when(customerService.findByAddress(address)).thenReturn(java.util.Optional.of(customerDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/address")
                        .param("address", address)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCustomer").value(customerDtoSend.getIdCustomer()))
                .andExpect(jsonPath("$.name").value(customerDtoSend.getName()))
                .andExpect(jsonPath("$.email").value(customerDtoSend.getEmail()))
                .andExpect(jsonPath("$.address").value(customerDtoSend.getAddress()))
                .andExpect(jsonPath("$.orders").isEmpty());
    }
    @Test
    void getByAddressNotFound() throws Exception {
        String address = "address 123";

        when(customerService.findByAddress(address)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/address")
                        .param("address", address)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Customer not found"));
    }

    @Test
    void save() throws Exception {

        when(customerService.save(customerDtoSave)).thenReturn(customerDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(CONTENT_TYPE)
                        .content(new ObjectMapper().writeValueAsString(customerDtoSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCustomer").value(customerDtoSend.getIdCustomer()));
    }

    @Test
    void update() throws Exception {
        Long idCustomer = 1L;

        when(customerService.update(customerDtoSave, idCustomer)).thenReturn(customerDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL + "/" + idCustomer)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(customerDtoSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCustomer").value(customerDtoSend.getIdCustomer()));
    }

    @Test
    void delete() throws Exception {
        long idCustomer = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/" + idCustomer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Customer deleted"));
    }

    private Page<CustomerDtoSend> createPageWithSingleProduct(CustomerDtoSend customerDtoSend) {
        List<CustomerDtoSend> customerDtoSendsList = new ArrayList<>();
        customerDtoSendsList.add(customerDtoSend);
        return new PageImpl<>(customerDtoSendsList);
    }
}