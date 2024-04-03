package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.OrderItemDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderItemDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.OrderItem;
import com.ventas.tienda.model.entities.Product;
import com.ventas.tienda.model.mapper.OrderItemMapper;
import com.ventas.tienda.repository.OrderItemRepository;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.repository.ProductRepository;
import com.ventas.tienda.service.impl.OrderItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    OrderItem orderItem;
    OrderItemDtoSend orderItemDtoSend;
    OrderItemDtoSave orderItemDtoSave;

    @BeforeEach
    void setUp() {
        orderItem = OrderItem.builder()
                .idOrderItem(1L)
                .quantity(1)
                .unitPrice(1.0)
                .order(Order.builder()
                        .idOrder(1L)
                        .build())
                .product(Product.builder()
                        .idProduct(1L)
                        .build())
                .build();
        orderItemDtoSend = OrderItemDtoSend.builder()
                .idOrderItem(1L)
                .quantity(1)
                .unitPrice(1.0)
                .idOrder(1L)
                .idProduct(1L)
                .build();
        orderItemDtoSave = OrderItemDtoSave.builder()
                .quantity(1)
                .unitPrice(1.0)
                .build();
    }


    @Test
    void givenOrderId_whenFindByOrder_IdOrder_thenReturnOrderItemDtoSendList() {
        Long idOrder = 1L;
        when(orderItemRepository.findByProduct_IdProduct(idOrder)).thenReturn(List.of(orderItem));
        when(orderItemMapper.ListEntityToDtoSend(List.of(orderItem))).thenReturn(List.of(orderItemDtoSend));

        List<OrderItemDtoSend> orderItemDtoSendList = orderItemService.findByProduct_IdProduct(idOrder);

        assertEquals(List.of(orderItemDtoSend), orderItemDtoSendList);
        assertEquals(1, orderItemDtoSendList.size());
    }

    @Test
    void givenProductId_whenFindByProduct_IdProduct_thenReturnOrderItemDtoSendList() {
        Long idProduct = 1L;
        when(orderItemRepository.findByProduct_IdProduct(idProduct)).thenReturn(List.of(orderItem));
        when(orderItemMapper.ListEntityToDtoSend(List.of(orderItem))).thenReturn(List.of(orderItemDtoSend));

        List<OrderItemDtoSend> orderItemDtoSendList = orderItemService.findByProduct_IdProduct(idProduct);

        assertEquals(List.of(orderItemDtoSend), orderItemDtoSendList);
        assertEquals(1, orderItemDtoSendList.size());
    }

    @Test
    void givenProductId_whenSumTotalSalesByProduct_thenReturnTotalSales() {
        Long idProduct = 1L;
        when(orderItemRepository.sumTotalSalesByProduct(idProduct)).thenReturn(1.0);

        Double totalSales = orderItemService.sumTotalSalesByProduct(idProduct);

        assertEquals(1.0, totalSales);
    }

    @Test
    void givenOrderItemDtoSaveAndProductIdAndOrderId_whenSave_thenReturnOrderItemDtoSend() {
        Long idProduct = 1L;
        Long idOrder = 1L;

        when(orderItemMapper.dtoSaveToEntity(orderItemDtoSave)).thenReturn(orderItem);
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        when(orderItemMapper.EntityToDtoSend(orderItem)).thenReturn(orderItemDtoSend);

        System.out.println(orderItem.getOrder().getIdOrder());

        OrderItemDtoSend orderItemDtoSend = orderItemService.save(orderItemDtoSave, idProduct, idOrder);

        assertNotNull(orderItemDtoSend);
        assertEquals(orderItem.getOrder().getIdOrder(), orderItemDtoSend.getIdOrder());
        assertEquals(orderItem.getProduct().getIdProduct(), orderItemDtoSend.getIdProduct());
        assertEquals(orderItem.getIdOrderItem(), orderItemDtoSend.getIdOrderItem());
    }
}