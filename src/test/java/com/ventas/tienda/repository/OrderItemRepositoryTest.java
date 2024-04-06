package com.ventas.tienda.repository;

import com.ventas.tienda.IntegrationDbRepositoryTest;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.OrderItem;
import com.ventas.tienda.model.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OrderItemRepositoryTest extends IntegrationDbRepositoryTest {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    OrderItemRepositoryTest(OrderItemRepository orderItemRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
    }

    void createOrderItem() {
        Order order = Order.builder()
                .status(Order.Status.PENDING)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        Product product = Product.builder()
                .name("Product")
                .price(100.0)
                .stock(10)
                .build();
        productRepository.save(product);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .unitPrice(100.0)
                .build();
        orderItemRepository.save(orderItem);

        OrderItem orderItem2 = OrderItem.builder()
                .order(order)
                .product(product)
                .unitPrice(200.0)
                .quantity(3)
                .build();
        orderItemRepository.save(orderItem2);

        orderItemRepository.flush();
    }

    @Test
    void givenIdOrder_whenFindByOrder_IdOrder_thenReturnOrderItems() {
        createOrderItem();

        Long idOrder = 1L;

        List<OrderItem> orderItems = orderItemRepository.findByOrder_IdOrder(idOrder);

        assertEquals(2, orderItems.size());
        assertEquals(2, orderItems.get(0).getQuantity());
        assertEquals(3, orderItems.get(1).getQuantity());
    }

    @Test
    void givenOrderItem_whenSave_thenOrderItemId() {
        createOrderItem();

        OrderItem orderItem = orderItemRepository.findAll().get(0);

        assertEquals(1, orderItem.getIdOrderItem());
    }

    @Test
    void givenIdProduct_whenFindByProduct_IdProduct_thenReturnOrderItems() {
        createOrderItem();

        Long idProduct = 1L;

        List<OrderItem> orderItems = orderItemRepository.findByOrder_IdOrder(idProduct);

        assertEquals(2, orderItems.size());
        assertEquals(2, orderItems.get(0).getQuantity());
        assertEquals(3, orderItems.get(1).getQuantity());
    }

    @Test
    void givenIdProduct_whenSumTotalSalesByProduct_thenReturnTotalSales() {
        createOrderItem();

        Long idProduct = 1L;

        Double totalSales = orderItemRepository.sumTotalSalesByProduct(idProduct);

        assertEquals(5.0, totalSales);
    }

    @Test
    void givenIdOrderItem_whenFindByIdO_thenReturnOrderItem() {
        createOrderItem();

        Long idOrderItem = 1L;

        Optional<OrderItem> orderItem = orderItemRepository.findById(idOrderItem);

        assertEquals(1, orderItem.get().getIdOrderItem());
        assertEquals(2, orderItem.get().getQuantity());
    }

    @Test
    void whenFindAll_thenReturnOrderItems() {
        createOrderItem();

        List<OrderItem> orderItems = orderItemRepository.findAll();

        assertEquals(2, orderItems.size());
    }

    @Test
    void givenIdOrderItem_whenDeleteById_thenOrderItemDeleted() {
        createOrderItem();

        Long idOrderItem = 1L;

        orderItemRepository.deleteById(idOrderItem);

        assertEquals(1, orderItemRepository.findAll().size());
    }
}