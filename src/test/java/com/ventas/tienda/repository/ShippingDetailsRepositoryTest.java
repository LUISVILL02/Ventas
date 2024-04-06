package com.ventas.tienda.repository;

import com.ventas.tienda.IntegrationDbRepositoryTest;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.ShippingDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        shippingDetailsRepository.deleteAll();
    }

    void mockData() {
        Order order = Order.builder()
                .idOrder(1L)
                .build();
        ShippingDetails shippingDetails =  ShippingDetails.builder()
                .carrier("Carrier")
                .guideNumber(123456L)
                .address("Address")
                .order(order)
                .build();
        orderRepository.save(order);
        shippingDetailsRepository.save(shippingDetails);
    }

    @Test
    void givenShippingDetails_whenSave_thenDetailsId() {
        mockData();
        ShippingDetails shippingDetails = shippingDetailsRepository.findAll().get(0);
        assertNotNull(shippingDetails.getIdDetail());
    }
}