package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.OrderDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.entities.Customer;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.mapper.OrderMapper;
import com.ventas.tienda.repository.CustomerRepository;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    Order order;
    OrderDtoSend orderDtoSend;
    OrderDtoSave orderDtoSave;

    private Page<Order> createPageWithSingleProduct(Order order) {
        List<Order> ordersList = new ArrayList<>();
        ordersList.add(order);
        return new PageImpl<>(ordersList);
    }

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .idOrder(1L)
                .orderDate(LocalDateTime.now())
                .status(Order.Status.PENDING)
                .customer(Customer.builder()
                        .idCustomer(1L)
                        .build())
                .build();
        orderDtoSend = OrderDtoSend.builder()
                .idOrder(1L)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .idCustomer(1L)
                .build();
        orderDtoSave = OrderDtoSave.builder()
                .status(Order.Status.PENDING)
                .build();
    }

    @Test
    void givenOrderDateBetween_whenFindByOrderDateBetween_thenReturnOrderDtoSendPage() {
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 31, 23, 59);

        Page<Order> pageOrder = createPageWithSingleProduct(order);
        when(orderRepository.findByOrderDateBetween(PageRequest.of(0,10), startDate, endDate)).thenReturn(pageOrder);
        when(orderMapper.EntityToDtoSend(order)).thenReturn(orderDtoSend);

        Page<OrderDtoSend> result = orderService.findByOderDateBetween(startDate, endDate);
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void givenStatusAndCustomer_whenFindByStatusAndCustomer_thenReturnOrderDtoSendPage() {
        Order.Status status = Order.Status.PENDING;
        Long idCustomer = 1L;

        Page<Order> pageOrder = createPageWithSingleProduct(order);
        when(orderRepository.findByStatusAndCustomer_IdCustomer(PageRequest.of(0,10), status, idCustomer)).thenReturn(pageOrder);
        when(orderMapper.EntityToDtoSend(order)).thenReturn(orderDtoSend);

        Page<OrderDtoSend> result = orderService.findByStatusAndCustomer(status, idCustomer);
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void givenCustomerWithItems_whenFindByCustomerWithItems_thenReturnOrderDtoSendPage() {
        Long idCustomer = 1L;

        Page<Order> pageOrder = createPageWithSingleProduct(order);
        when(orderRepository.findByCustomerWithItems(PageRequest.of(0,10), idCustomer)).thenReturn(pageOrder);
        when(orderMapper.EntityToDtoSend(order)).thenReturn(orderDtoSend);

        Page<OrderDtoSend> result = orderService.findByCustomerWithItems(idCustomer);
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertEquals(order.getCustomer().getIdCustomer(), result.getContent().get(0).getIdCustomer());
    }

    @Test
    void givenOrderDtoSave_whenUpdate_thenReturnOrderDtoSend() {
        Long id = 1L;
        Customer customer = Customer.builder()
                .idCustomer(id)
                .build();

        when(orderMapper.dtoSaveToEntity(orderDtoSave)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.EntityToDtoSend(order)).thenReturn(orderDtoSend);


        OrderDtoSend result = orderService.update(orderDtoSave, id);
        assertNotNull(result);
        assertEquals(orderDtoSend, result);
        assertEquals(orderDtoSend.getIdCustomer(), customer.getIdCustomer());
    }

    @Test
    void givenIdCustomer_whenSave_thenReturnRuntimeException() {
        Long idCustomer = 1L;
        when(customerRepository.findById(idCustomer)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.save(orderDtoSave, idCustomer));
    }
}