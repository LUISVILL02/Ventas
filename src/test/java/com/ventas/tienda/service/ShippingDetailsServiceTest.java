package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.ShippingDetailsDtoSave;
import com.ventas.tienda.model.Dtos.send.ShippingDetailsDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.ShippingDetails;
import com.ventas.tienda.model.mapper.ShippingDetailsMapper;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.repository.ShippingDetailsRepository;
import com.ventas.tienda.service.impl.ShippingDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShippingDetailsServiceTest {
    @Mock
    ShippingDetailsRepository shippingDetailsRepository;
    @Mock
    ShippingDetailsMapper  shippingDetailsMapper;
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    ShippingDetailsServiceImpl shippingDetailsService;

    ShippingDetails shippingDetails;
    ShippingDetailsDtoSave shippingDetailsDtoSave;
    ShippingDetailsDtoSend shippingDetailsDtoSend;
    Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .idOrder(1L)
                .details(shippingDetails)
                .status(Order.Status.PENDING)
                .build();
        shippingDetails = ShippingDetails.builder()
                .idDetail(1L)
                .carrier("carrier")
                .address("address")
                .guideNumber(3444343L)
                .order(order)
                .build();
        shippingDetailsDtoSave = ShippingDetailsDtoSave.builder()
                .address("address")
                .carrier("carrier")
                .guideNumber(3444343)
                .build();
        shippingDetailsDtoSend = ShippingDetailsDtoSend.builder()
                .idDetail(1L)
                .address("address")
                .carrier("carrier")
                .guideNumber(3444343L)
                .idOrder(1L)
                .build();
    }

    @Test
    void givenIdOrder_whenFindByOrder_IdOrder_thenReturnShippingDetailsDtoSendList() {
        when(shippingDetailsRepository.findByOrder_IdOrder(order.getIdOrder())).thenReturn(List.of(shippingDetails));
        when(shippingDetailsMapper.ListEntityToDtoSend(List.of(shippingDetails))).thenReturn(List.of(shippingDetailsDtoSend));

        List<ShippingDetailsDtoSend> shippingDetailsDtoSendList = shippingDetailsService.findByOrder_IdOrder(order.getIdOrder());

        assertEquals(List.of(shippingDetailsDtoSend), shippingDetailsDtoSendList);
        assertEquals(1, shippingDetailsDtoSendList.size());
        assertEquals(shippingDetailsDtoSend.getGuideNumber(), shippingDetailsDtoSendList.get(0).getGuideNumber());
    }

    @Test
    void givenCarrier_whenFindByCarrier_thenReturnShippingDetailsDtoSendList() {
        when(shippingDetailsRepository.findByCarrier(shippingDetails.getCarrier())).thenReturn(List.of(shippingDetails));
        when(shippingDetailsMapper.ListEntityToDtoSend(List.of(shippingDetails))).thenReturn(List.of(shippingDetailsDtoSend));

        List<ShippingDetailsDtoSend> shippingDetailsDtoSendList = shippingDetailsService.findByCarrier(shippingDetails.getCarrier());

        assertEquals(List.of(shippingDetailsDtoSend), shippingDetailsDtoSendList);
        assertEquals(1, shippingDetailsDtoSendList.size());
        assertEquals(shippingDetailsDtoSend.getCarrier(), shippingDetailsDtoSendList.get(0).getCarrier());
    }

    @Test
    void givenStatus_whenFindByOrder_Status_thenReturnShippingDetailsDtoSendList() {
        when(shippingDetailsRepository.findByOrder_Status(order.getStatus())).thenReturn(List.of(shippingDetails));
        when(shippingDetailsMapper.ListEntityToDtoSend(List.of(shippingDetails))).thenReturn(List.of(shippingDetailsDtoSend));

        List<ShippingDetailsDtoSend> shippingDetailsDtoSendList = shippingDetailsService.findByOrder_Status(order.getStatus());

        assertEquals(List.of(shippingDetailsDtoSend), shippingDetailsDtoSendList);
        assertEquals(1, shippingDetailsDtoSendList.size());
        assertEquals(shippingDetails.getOrder().getStatus(), order.getStatus());
    }

    @Test
    void givenShippingDetailsDtoSaveAndIdOrder_whenSave_thenReturnShippingDetailsDtoSend() {
        when(orderRepository.findById(order.getIdOrder())).thenReturn(Optional.of(order));
        when(shippingDetailsMapper.dtoSaveToEntity(shippingDetailsDtoSave)).thenReturn(shippingDetails);
        when(shippingDetailsRepository.save(shippingDetails)).thenReturn(shippingDetails);
        when(shippingDetailsMapper.EntityToDtoSend(shippingDetails)).thenReturn(shippingDetailsDtoSend);


        ShippingDetailsDtoSend shippingDetailsDtoSendTest = shippingDetailsService.save(shippingDetailsDtoSave, order.getIdOrder());

        assertEquals(shippingDetailsDtoSend, shippingDetailsDtoSendTest);
        assertEquals(shippingDetailsDtoSend.getGuideNumber(), shippingDetails.getGuideNumber());
        assertEquals(shippingDetails.getOrder().getIdOrder(), order.getIdOrder());
    }

    @Test
    void givenShippingDetailsDtoSaveAndId_whenSave_thenThrowRuntimeException() {
        when(orderRepository.findById(order.getIdOrder())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> shippingDetailsService.save(shippingDetailsDtoSave, order.getIdOrder()));
    }

    @Test
    void givenShippingDetailsDtoSaveAndId_whenUpdate_thenReturnShippingDetailsDtoSend() {
        when(shippingDetailsRepository.findById(shippingDetails.getIdDetail())).thenReturn(Optional.of(shippingDetails));
        when(shippingDetailsMapper.dtoSaveToEntity(shippingDetailsDtoSave)).thenReturn(shippingDetails);
        when(shippingDetailsMapper.EntityToDtoSend(shippingDetails)).thenReturn(shippingDetailsDtoSend);
        when(shippingDetailsRepository.save(any(ShippingDetails.class))).thenReturn(shippingDetails);

        ShippingDetailsDtoSend shippingDetailsDtoSendTest = shippingDetailsService.update(shippingDetailsDtoSave, shippingDetails.getIdDetail());

        assertEquals(shippingDetailsDtoSend, shippingDetailsDtoSendTest);
        assertNotNull(shippingDetailsDtoSendTest);
        assertEquals(shippingDetails.getIdDetail(), shippingDetailsDtoSendTest.getIdDetail());
    }
}