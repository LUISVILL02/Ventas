package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.OrderItemDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.Dtos.send.OrderItemDtoSend;
import com.ventas.tienda.service.OrderItemService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderItemController.class)
@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderItemService orderItemService;

    OrderItemDtoSave orderItemDtoSave;
    OrderItemDtoSend orderItemDtoSend;

    private static final String URL = "/api/v1/order-items";
    private static final String CONTENT_TYPE = "application/json";

    @BeforeEach
    void setUp() {
        orderItemDtoSave = OrderItemDtoSave.builder()
                .quantity(1)
                .unitPrice(1.0)
                .build();
        orderItemDtoSend = OrderItemDtoSend.builder()
                .idOrderItem(1L)
                .idOrder(1L)
                .idProduct(1L)
                .quantity(1)
                .unitPrice(1.0)
                .build();
    }

    @Test
    void getById() throws Exception {
        Long idOrderItem = 1L;

        when(orderItemService.findById(idOrderItem)).thenReturn(Optional.ofNullable(orderItemDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/"+  idOrderItem)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOrderItem").value(orderItemDtoSend.getIdOrderItem()))
                .andExpect(jsonPath("$.idOrder").value(orderItemDtoSend.getIdOrder()))
                .andExpect(jsonPath("$.idProduct").value(orderItemDtoSend.getIdProduct()))
                .andExpect(jsonPath("$.quantity").value(orderItemDtoSend.getQuantity()))
                .andExpect(jsonPath("$.unitPrice").value(orderItemDtoSend.getUnitPrice()));

    }

    @Test
    void getAll() throws Exception {
        Page<OrderItemDtoSend> orderItemDtoSendPage = createPageWithSingleOrderItem(orderItemDtoSend);

        when(orderItemService.findAll()).thenReturn(orderItemDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idOrderItem").value(orderItemDtoSend.getIdOrderItem()))
                .andExpect(jsonPath("$.content[0].unitPrice").value(orderItemDtoSend.getUnitPrice()));

    }

    @Test
    void getByOrderId() throws Exception {
        Long idOrder = 1L;

        when(orderItemService.findByOrder_IdOrder(idOrder)).thenReturn(List.of(orderItemDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/order/" + idOrder)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOrder").value(orderItemDtoSend.getIdOrder()));

    }

    @Test
    void getByProductId() throws Exception {
        Long idProduct = 1L;

        when(orderItemService.findByProduct_IdProduct(idProduct)).thenReturn(List.of(orderItemDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/product/" + idProduct)
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOrder").value(orderItemDtoSend.getIdOrder()));

    }

    @Test
    void save() throws Exception {
        Long idProduct = 1L;
        Long idOrder = 1L;

        when(orderItemService.save(orderItemDtoSave, idProduct, idOrder)).thenReturn(orderItemDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL + "/" + idProduct + "/" + idOrder)
                        .contentType(CONTENT_TYPE)
                        .content(new ObjectMapper().writeValueAsString(orderItemDtoSave))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOrderItem").value(orderItemDtoSend.getIdOrderItem()));
    }

    @Test
    void update() throws Exception {
        Long idOrderItem = 1L;

        when(orderItemService.update(orderItemDtoSave, idOrderItem)).thenReturn(orderItemDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL + "/" + idOrderItem)
                .contentType(CONTENT_TYPE)
                .content(new  ObjectMapper().writeValueAsString(orderItemDtoSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOrderItem").value(orderItemDtoSend.getIdOrderItem()));
    }

    @Test
    void delete() throws Exception {

        long idIOrderItem = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                .delete(URL + "/" + idIOrderItem)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());

    }

    private Page<OrderItemDtoSend> createPageWithSingleOrderItem(OrderItemDtoSend orderItemDtoSend) {
        List<OrderItemDtoSend> orderItemDtoSendLIst = new ArrayList<>();
        orderItemDtoSendLIst.add(orderItemDtoSend);
        return new PageImpl<>(orderItemDtoSendLIst);
    }
}