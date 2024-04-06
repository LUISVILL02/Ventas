package com.ventas.tienda.repository;

import com.ventas.tienda.IntegrationDbRepositoryTest;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.ShippingDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ShippingDetailsRepositoryTest extends IntegrationDbRepositoryTest {

    private final ShippingDetailsRepository shippingDetailsRepository;
    private final OrderRepository orderRepository;

    @Autowired
    ShippingDetailsRepositoryTest(ShippingDetailsRepository shippingDetailsRepository, OrderRepository orderRepository) {
        this.shippingDetailsRepository = shippingDetailsRepository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        shippingDetailsRepository.deleteAll();
    }

    void mockData() {
        ShippingDetails shippingDetails =  ShippingDetails.builder()
                .carrier("Carrier")
                .guideNumber(123456L)
                .address("Address")
                .build();

        shippingDetailsRepository.save(shippingDetails);

        ShippingDetails shippingDetails2=  ShippingDetails.builder()
                .carrier("Carrier2")
                .guideNumber(1234562L)
                .address("Address2")
                .build();
        shippingDetailsRepository.save(shippingDetails2);

        shippingDetailsRepository.flush();
    }

    @Test
    void givenShippingDetails_whenSave_thenDetailsId() {
        mockData();
        ShippingDetails shippingDetails = shippingDetailsRepository.findAll().get(0);
        assertNotNull(shippingDetails.getIdDetail());
    }

    @Test
    void givenShippingDetails_whenFindByOrder_IdOrder_thenDetails() {
        mockData();
        Order order = Order.builder()
                .status(Order.Status.PENDING)
                .orderDate(LocalDateTime.now())
                .build();
        Order orderSave = orderRepository.save(order);

        ShippingDetails shippingDetails = shippingDetailsRepository.findAll().get(0);
        shippingDetails.setOrder(orderSave);

        shippingDetailsRepository.flush();

        List<ShippingDetails> listDetails = shippingDetailsRepository.findByOrder_IdOrder(orderSave.getIdOrder());

        assertThat(listDetails).isNotEmpty();
        assertEquals(listDetails.get(0).getOrder().getIdOrder(), orderSave.getIdOrder());
        assertNotEquals(1234562L, (long) listDetails.get(0).getGuideNumber());
    }

    @Test
    void givenShippingDetails_whenFindByCarrier_thenDetails() {
        mockData();

        List<ShippingDetails> listDetails = shippingDetailsRepository.findByCarrier("Carrier");

        assertThat(listDetails).isNotEmpty();
        assertEquals("Carrier", listDetails.get(0).getCarrier());
    }

    @Test
    void givenShippingDetails_whenFindByOrder_Status_thenDetails() {
        mockData();
        Order order = Order.builder()
                .status(Order.Status.PENDING)
                .orderDate(LocalDateTime.now())
                .build();
        Order orderSave = orderRepository.save(order);

        ShippingDetails shippingDetails = shippingDetailsRepository.findAll().get(0);
        shippingDetails.setOrder(orderSave);

        shippingDetailsRepository.flush();

        List<ShippingDetails> listDetails = shippingDetailsRepository.findByOrder_Status(orderSave.getStatus());

        assertThat(listDetails).isNotEmpty();
        assertEquals(orderSave.getStatus(), listDetails.get(0).getOrder().getStatus());
        assertNotEquals(1234562L, (long) listDetails.get(0).getGuideNumber());
    }

    @Test
    void givenIdShippingDetails_whenFindById_thenDetails(){
        mockData();
        ShippingDetails shippingDetails = shippingDetailsRepository.findAll().get(0);
        Optional<ShippingDetails> shippingDetailsFind = shippingDetailsRepository.findById(shippingDetails.getIdDetail());

        assertEquals(shippingDetails.getIdDetail(), shippingDetailsFind.get().getIdDetail());
    }

    @Test
    void givenIdDetails_whenDeleteById_thenDetails(){
        mockData();

        ShippingDetails shippingDetails = shippingDetailsRepository.findAll().get(0);

        shippingDetailsRepository.deleteById(shippingDetails.getIdDetail());

        Optional<ShippingDetails> listDetails = shippingDetailsRepository.findById(shippingDetails.getIdDetail());

        assertThat(listDetails).isEmpty();
    }
}