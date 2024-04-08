package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.ProductDtoSave;
import com.ventas.tienda.model.Dtos.send.ProductDtoSend;
import com.ventas.tienda.service.ProductService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    private static final String URL = "/api/v1/products";
    private static final String CONTENT_TYPE = "application/json";

    ProductDtoSave productDtoSave;
    ProductDtoSend productDtoSend;
    @BeforeEach
    void setUp() {
        productDtoSave = ProductDtoSave.builder()
                .name("Product")
                .price(100.0)
                .stock(10)
                .build();
        productDtoSend = ProductDtoSend.builder()
                .idProduct(1L)
                .name("Product")
                .price(100.0)
                .stock(10)
                .build();
    }

    @Test
    void getById() throws Exception {
        Long idProduct = 1L;
        when(productService.findById(idProduct)).thenReturn(Optional.ofNullable(productDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/" + idProduct)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduct").value(productDtoSend.getIdProduct()));


    }
    @Test
    void getByIdNotFound() throws Exception {
        long idProduct = 100L;

        when(productService.findById(idProduct)).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/" + idProduct)
                .accept(CONTENT_TYPE))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));
    }

    @Test
    void getByName() throws Exception {
        String name = "Product";
        Page<ProductDtoSend> productDtoSends = createPageWithSingleProduct(productDtoSend);


        when(productService.findByName(name)).thenReturn(productDtoSends);

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/name")
                .param("name", name)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(productDtoSend.getName()));
    }

    @Test
    void getByStockGreaterThan() throws Exception {
        Page<ProductDtoSend> productDtoSends = createPageWithSingleProduct(productDtoSend);
        Integer stock = 0;

        when(productService.findByStockGreaterThan(stock)).thenReturn(productDtoSends);

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/stock")
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].stock").value(productDtoSend.getStock()))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void save() throws Exception {
        when(productService.save(productDtoSave)).thenReturn(productDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(productDtoSave))
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(productDtoSend.getName()));
    }

    @Test
    void update() throws Exception {
        Long idProduct = 1L;

        when(productService.update(productDtoSave, idProduct)).thenReturn(productDtoSend);

mockMvc.perform(MockMvcRequestBuilders
                .put(URL + "/" + idProduct)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(productDtoSave))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productDtoSend.getName()));
    }

    @Test
    void delete() throws Exception {
        Long idProduct = 1L;

        doNothing().when(productService).deleteById(idProduct);

        mockMvc.perform(MockMvcRequestBuilders
                .delete(URL + "/" + idProduct)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted"));
    }

    private Page<ProductDtoSend> createPageWithSingleProduct(ProductDtoSend productSend) {
        List<ProductDtoSend> productList = new ArrayList<>();
        productList.add(productSend);
        return new PageImpl<>(productList);
    }
}