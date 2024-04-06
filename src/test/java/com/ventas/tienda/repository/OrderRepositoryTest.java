package com.ventas.tienda.repository;

import com.ventas.tienda.IntegrationDbRepositoryTest;
import com.ventas.tienda.model.entities.Customer;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderRepositoryTest extends IntegrationDbRepositoryTest {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepositoryTest(OrderRepository orderRepository, CustomerRepository customerRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.findAll();
    }

    void createOrder() {
        Order order = Order.builder()
                .status(Order.Status.PENDING)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        Order order2 = Order.builder()
                .status(Order.Status.PENDING)
                .orderDate(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        orderRepository.save(order2);

        Customer customer = Customer.builder()
                .name("Customer")
                .email("vill@gmail.com")
                .address("Calle 123")
                .build();
        customerRepository.save(customer);

        orderRepository.flush();
    }

    @Test
    void givenOrder_whenSave_thenOrderId() {
        createOrder();

        Order order = orderRepository.findAll().get(0);

        assertNotNull(order.getIdOrder());
    }

    @Test
    void givenStatusAndCustomer_whenFindByStatusAndCustomer_thenReturnOrder() {
        createOrder();
        Pageable pageable = PageRequest.of(0, 1);

        Customer customer = customerRepository.findAll().get(0);
        Order order1 = orderRepository.findAll().get(0);

        order1.setCustomer(customer);

        Page<Order> order = orderRepository.findByStatusAndCustomer_IdCustomer(pageable, Order.Status.PENDING, customer.getIdCustomer());

        assertNotNull(order);
        assertEquals(order.getContent().get(0).getStatus(), Order.Status.PENDING);
        assertEquals(order.getContent().get(0).getCustomer().getIdCustomer(), customer.getIdCustomer());
    }

    @Test
    void givenTwoDates_whenFindByOrderDateBetween_thenReturnOrder() {
        createOrder();
        Pageable pageable = PageRequest.of(0, 1);

        Order order1 = orderRepository.findAll().get(0);

        Page<Order> order = orderRepository.findByOrderDateBetween(pageable, LocalDateTime.of(2021, 1, 1, 0, 0), LocalDateTime.now());

        assertNotNull(order);
        assertEquals(order.getContent().get(0).getOrderDate(), order1.getOrderDate());
    }

    @Test
    void givenCustomer_whenFindByCustomerWithItems_thenReturnOrder() {
        createOrder();
        Pageable pageable = PageRequest.of(0, 1);

        OrderItem orderItem = OrderItem.builder()
                .quantity(1)
                .unitPrice(100.0)
                .build();

        Customer customer = customerRepository.findAll().get(0);
        Order order1 = orderRepository.findAll().get(0);

        order1.setCustomer(customer);

        orderItem.setOrder(order1);
        orderItemRepository.save(orderItem);

        Page<Order> order = orderRepository.findByCustomerWithItems(pageable, customer.getIdCustomer());

        assertNotNull(order);
        assertEquals(order.getContent().get(0).getCustomer().getIdCustomer(), customer.getIdCustomer());
    }

    @Test
    void givenIdOrder_whenFindByIdOrder_thenReturnOrder() {
        createOrder();

        Order order = orderRepository.findAll().get(0);
        Optional<Order> orderFind = orderRepository.findById(order.getIdOrder());

        assertNotNull(orderFind);
        assertEquals(order.getIdOrder(), orderFind.get().getIdOrder());
    }

    @Test
    void givenIdOrder_whenFindByIdOrder_thenReturnEmpty() {
        createOrder();

        Optional<Order> orderFind = orderRepository.findById(3L);

        assertEquals(Optional.empty(), orderFind);
    }

    @Test
    void givenIdOrder_whenDeleteByIdOrder_thenOrder() {
        createOrder();

        Long idOrder = 1L;

        orderRepository.deleteById(idOrder);

        assertEquals(1, orderRepository.findAll().size());
    }
}