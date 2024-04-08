package com.ventas.tienda.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ventas.tienda.model.Dtos.save.PaymentDtoSave;
import com.ventas.tienda.model.Dtos.send.PaymentDtoSend;
import com.ventas.tienda.model.entities.Payment;
import com.ventas.tienda.service.PaymentService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentService paymentService;

    PaymentDtoSave paymentDtoSave;
    PaymentDtoSend paymentDtoSend;

    private static final String URL = "/api/v1/payments";
    private static final String CONTENT_TYPE = "application/json";

    @BeforeEach
    void setUp() {
        paymentDtoSave = PaymentDtoSave.builder()
                .paymentMethod("CREDIT_CARD")
                .paymentDate(LocalDate.of(2021, 10, 10))
                .paymentTotal(100.0)
                .build();
        paymentDtoSend = PaymentDtoSend.builder()
                .paymentMethod("CREDIT_CARD")
                .paymentDate(LocalDate.of(2021, 10, 10))
                .paymentTotal(100.0)
                .idOrder(1L)
                .idPayment(1L)
                .build();
    }

    @Test
    void getById() throws Exception {
        Long idPayment = 1L;
        when(paymentService.findById(idPayment)).thenReturn(java.util.Optional.of(paymentDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/" + idPayment)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPayment").value(paymentDtoSend.getIdPayment()));
    }
    @Test
    void getByIdNotFound() throws Exception {
        Long idPayment = 1L;
        when(paymentService.findById(idPayment)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/" + idPayment)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Payment not found"));
    }

    @Test
    void getAll() throws Exception {
        Page<PaymentDtoSend> paymentDtoSendPage = createPageWithSinglePayment(paymentDtoSend);

        when(paymentService.findAll()).thenReturn(paymentDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idPayment").value(paymentDtoSend.getIdPayment()));
    }

    @Test
    void getByOrder() throws Exception {
        Long idOrder = 1L;
        Page<PaymentDtoSend> paymentDtoSendPage = createPageWithSinglePayment(paymentDtoSend);

        when(paymentService.findByPaymentMethodAndOrder_IdOrder(Payment.MethodPay.CREDIT_CARD, idOrder)).thenReturn(paymentDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/order/" + idOrder + "/CREDIT_CARD")
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idPayment").value(paymentDtoSend.getIdPayment()));
    }

    @Test
    void getByDateRange() throws Exception {
        LocalDate startDate = LocalDate.of(2020, 10, 10);
        LocalDate endDate = LocalDate.of(2022, 10, 10);
        Page<PaymentDtoSend> paymentDtoSendPage = createPageWithSinglePayment(paymentDtoSend);

        when(paymentService.findByPaymentDateBetween(startDate, endDate)).thenReturn(paymentDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idPayment").value(paymentDtoSend.getIdPayment()));
    }

    @Test
    void save() throws Exception {
        Long idOrder = 1L;
        when(paymentService.save(paymentDtoSave, idOrder)).thenReturn(paymentDtoSend);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonBody = objectMapper.writeValueAsString(paymentDtoSave);

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL + "/" + idOrder)
                .contentType(CONTENT_TYPE)
                .content(jsonBody)
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPayment").value(paymentDtoSend.getIdPayment()));
    }

    @Test
    void update() throws Exception {
        Long idPayment = 1L;
        when(paymentService.update(paymentDtoSave, idPayment)).thenReturn(paymentDtoSend);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonBody = objectMapper.writeValueAsString(paymentDtoSave);

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL + "/" + idPayment)
                .contentType(CONTENT_TYPE)
                .content(jsonBody)
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPayment").value(paymentDtoSend.getIdPayment()));
    }

    @Test
    void delete() throws Exception {
        long idPayment = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                .delete(URL + "/" + idPayment)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    private Page<PaymentDtoSend> createPageWithSinglePayment(PaymentDtoSend paymentDtoSend) {
        List<PaymentDtoSend> paymentDtoSendLIst = new ArrayList<>();
        paymentDtoSendLIst.add(paymentDtoSend);
        return new PageImpl<>(paymentDtoSendLIst);
    }
}