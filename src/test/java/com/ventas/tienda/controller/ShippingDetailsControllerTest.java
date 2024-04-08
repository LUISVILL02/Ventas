package com.ventas.tienda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ventas.tienda.model.Dtos.save.ShippingDetailsDtoSave;
import com.ventas.tienda.model.Dtos.send.ShippingDetailsDtoSend;
import com.ventas.tienda.service.ShippingDetailsService;
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

@WebMvcTest(ShippingDetailsController.class)
@ExtendWith(MockitoExtension.class)
class ShippingDetailsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ShippingDetailsService shippingDetailsService;

    ShippingDetailsDtoSave shippingDetailsDtoSave;
    ShippingDetailsDtoSend shippingDetailsDtoSend;

    private static final String URL = "/api/v1/shipping-details";
    private static final String CONTENT_TYPE = "application/json";

    @BeforeEach
    void setUp() {
        shippingDetailsDtoSave = ShippingDetailsDtoSave.builder()
                .carrier("carrier")
                .guideNumber(34242)
                .address("address")
                .build();
        shippingDetailsDtoSend = ShippingDetailsDtoSend.builder()
                .idDetail(1L)
                .carrier("carrier")
                .guideNumber(34242L)
                .address("address")
                .idOrder(1L)
                .build();
    }

    @Test
    void getById() throws Exception {
        Long idDetails = 1L;
        when(shippingDetailsService.findById(idDetails)).thenReturn(java.util.Optional.of(shippingDetailsDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/" + idDetails)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDetail").value(shippingDetailsDtoSend.getIdDetail()));
    }
    @Test
    void getByIdNotFound() throws Exception {
        Long idDetails = 1L;
        when(shippingDetailsService.findById(idDetails)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/" + idDetails)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        Page<ShippingDetailsDtoSend> shippingDetailsDtoSendPage = createPageWithSingleShippingDetails(shippingDetailsDtoSend);
        when(shippingDetailsService.findAll()).thenReturn(shippingDetailsDtoSendPage);

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idDetail").value(shippingDetailsDtoSend.getIdDetail()));
    }

    @Test
    void getByOrderId() throws Exception {

        Long idOrder = 1L;

        when(shippingDetailsService.findByOrder_IdOrder(idOrder)).thenReturn(List.of(shippingDetailsDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/order/" + idOrder)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDetail").value(shippingDetailsDtoSend.getIdDetail()))
                .andExpect(jsonPath("$[0].idOrder").value(shippingDetailsDtoSend.getIdOrder()));
    }
    @Test
    void getByOrderIdNotFound() throws Exception {
        Long idOrder = 1L;

        when(shippingDetailsService.findByOrder_IdOrder(idOrder)).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/order/" + idOrder)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByCarrier() throws Exception {
        String carrier = "carrier";

        when(shippingDetailsService.findByCarrier(carrier)).thenReturn(List.of(shippingDetailsDtoSend));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/carrier")
                .contentType(CONTENT_TYPE)
                .param("nameCarrier", carrier))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDetail").value(shippingDetailsDtoSend.getIdDetail()))
                .andExpect(jsonPath("$[0].carrier").value(shippingDetailsDtoSend.getCarrier()));
    }
    @Test
    void getByCarrierNotFound() throws Exception {
        String carrier = "carrier";

        when(shippingDetailsService.findByCarrier(carrier)).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/carrier")
                .contentType(CONTENT_TYPE)
                .param("nameCarrier", carrier))
                .andExpect(status().isNotFound());
    }

    @Test
    void save() throws Exception {
        Long idOrder = 1L;

        when(shippingDetailsService.save(shippingDetailsDtoSave, idOrder)).thenReturn(shippingDetailsDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL + "/" + idOrder)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(shippingDetailsDtoSave))
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDetail").value(shippingDetailsDtoSend.getIdDetail()))
                .andExpect(jsonPath("$.idOrder").value(shippingDetailsDtoSend.getIdOrder()));
    }

    @Test
    void update() throws Exception {
        Long idDetails = 1L;

        when(shippingDetailsService.update(shippingDetailsDtoSave, idDetails)).thenReturn(shippingDetailsDtoSend);

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL + "/" + idDetails)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(shippingDetailsDtoSave))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDetail").value(shippingDetailsDtoSend.getIdDetail()));
    }

    @Test
    void delete() throws Exception {
        long idDetails = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                .delete(URL + "/" + idDetails)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    private Page<ShippingDetailsDtoSend> createPageWithSingleShippingDetails(ShippingDetailsDtoSend shippingDetailsDtoSend) {
        List<ShippingDetailsDtoSend> shippingDtoSendLIst = new ArrayList<>();
        shippingDtoSendLIst.add(shippingDetailsDtoSend);
        return new PageImpl<>(shippingDtoSendLIst);
    }
}