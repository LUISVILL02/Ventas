package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.OrderDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.Dtos.send.PaymentDtoSend;
import com.ventas.tienda.model.Dtos.send.ShippingDetailsDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.service.OrderService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    OrderDtoSave orderDtoSave;
    OrderDtoSend orderDtoSend;

    private static final String URL = "/api/v1/orders";
    private static final String CONTENT_TYPE = "application/json";

    @BeforeEach
    void setUp() {
        orderDtoSave = OrderDtoSave.builder()
                .status(Order.Status.SENT)
                .build();
        orderDtoSend = OrderDtoSend.builder()
                .idOrder(1L)
                .status("SENT")
                .orderDate(LocalDateTime.of(2021, 10, 10, 10, 10, 10))
                .idCustomer(1L)
                .orderItems(new ArrayList<>())
                .payment(PaymentDtoSend.builder()
                        .idOrder(1L)
                        .build())
                .shippingDetails(ShippingDetailsDtoSend.builder()
                        .idOrder(1L)
                        .build())
                .build();
    }

    @Test
    void getById() throws Exception {
        Long idOrder = 1L;

        when(orderService.findById(idOrder)).thenReturn(java.util.Optional.of(orderDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/"+ idOrder)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOrder").value(orderDtoSend.getIdOrder()))
                .andExpect(jsonPath("$.status").value(orderDtoSend.getStatus()))
                .andExpect(jsonPath("$.orderDate").value(orderDtoSend.getOrderDate().toString()))
                .andExpect(jsonPath("$.idCustomer").value(orderDtoSend.getIdCustomer()))
                .andExpect(jsonPath("$.orderItems").isArray())
                .andExpect(jsonPath("$.payment").isMap())
                .andExpect(jsonPath("$.shippingDetails").isMap());

    }
    @Test
    void getByIdNotFound() throws Exception {
        Long idOrder = 1L;

        when(orderService.findById(idOrder)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/"+ idOrder)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Order not found"));
    }

    @Test
    void getAll() throws Exception {

        Page<OrderDtoSend> orderDtoSendPage = createPageWithSingleOrder(orderDtoSend);

        when(orderService.findAll()).thenReturn(orderDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getByCustomer() throws Exception {
        Long idCustomer = 1L;

        Page<OrderDtoSend> orderDtoSendPage = createPageWithSingleOrder(orderDtoSend);

        when(orderService.findByCustomerWithItems(idCustomer)).thenReturn(orderDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/customer/" + idCustomer)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getByDateRange() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2020, 10, 10, 10, 10, 10);
        LocalDateTime endDate = LocalDateTime.of(2022, 10, 10, 10, 10, 10);

        Page<OrderDtoSend> orderDtoSendPage = createPageWithSingleOrder(orderDtoSend);

        when(orderService.findByOderDateBetween(startDate, endDate)).thenReturn(orderDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void save() throws Exception {
        Long idCustomer = 1L;

        when(orderService.save(orderDtoSave, idCustomer)).thenReturn(orderDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL + "/" + idCustomer)
                        .contentType(CONTENT_TYPE)
                        .content("{\"status\":\"SENT\"}")
                        .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOrder").value(orderDtoSend.getIdOrder()))
                .andExpect(jsonPath("$.orderDate").value(orderDtoSend.getOrderDate().toString()))
                .andExpect(jsonPath("$.idCustomer").value(orderDtoSend.getIdCustomer()));
    }

    @Test
    void update() throws Exception {
        Long idOrder = 1L;

        when(orderService.update(orderDtoSave, idOrder)).thenReturn(orderDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/" + idOrder)
                        .contentType(CONTENT_TYPE)
                        .content(new ObjectMapper().writeValueAsString(orderDtoSave))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOrder").value(orderDtoSend.getIdOrder()));
    }

    @Test
    void delete() throws Exception {
        long idOrder = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/" + idOrder)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Order delete"));
    }

    private Page<OrderDtoSend> createPageWithSingleOrder(OrderDtoSend orderDtoSend) {
        List<OrderDtoSend> orderDtoSendList = new ArrayList<>();
        orderDtoSendList.add(orderDtoSend);
        return new PageImpl<>(orderDtoSendList);
    }

}