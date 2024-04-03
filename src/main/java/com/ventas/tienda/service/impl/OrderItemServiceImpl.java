package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.Dtos.save.OrderItemDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderItemDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.OrderItem;
import com.ventas.tienda.model.entities.Product;
import com.ventas.tienda.model.mapper.OrderItemMapper;
import com.ventas.tienda.repository.OrderItemRepository;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.repository.ProductRepository;
import com.ventas.tienda.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl extends ServiceImp<OrderItemDtoSave, OrderItemDtoSend, OrderItem> implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    protected OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper, OrderRepository orderRepository, ProductRepository productRepository) {
        super(orderItemRepository, orderItemMapper);
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderItemDtoSend> findByOrder_IdOrder(Long idOrder) {
        List<OrderItem> orderItemList = orderItemRepository.findByOrder_IdOrder(idOrder);
        return orderItemMapper.ListEntityToDtoSend(orderItemList);
    }

    @Override
    public List<OrderItemDtoSend> findByProduct_IdProduct(Long idProduct) {
        List<OrderItem> orderItemList = orderItemRepository.findByProduct_IdProduct(idProduct);
        return orderItemMapper.ListEntityToDtoSend(orderItemList);
    }

    @Override
    public Double sumTotalSalesByProduct(Long idProduct) {
        return orderItemRepository.sumTotalSalesByProduct(idProduct);
    }

    @Override
    public OrderItemDtoSend save(OrderItemDtoSave orderItemDtoSave, Long idOProduct, Long idOrder){
        Optional<Order> order = orderRepository.findById(idOrder);
        Optional<Product> product = productRepository.findById(idOProduct);
        OrderItem orderItem = orderItemMapper.dtoSaveToEntity(orderItemDtoSave);
        if (order.isEmpty() || product.isEmpty())
            throw new RuntimeException("Order or Product not found");
        orderItem.setOrder(order.get());
        orderItem.setProduct(product.get());
        product.get().getOrderItems().add(orderItem);
        order.get().getOrderItems().add(orderItem);
        return orderItemMapper.EntityToDtoSend(orderItemRepository.save(orderItem));
    }

    @Override
    public OrderItemDtoSend update(OrderItemDtoSave orderItemDtoSave, Long id) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        if(orderItem.isEmpty())
            throw new RuntimeException("OrderItem not found");
        OrderItem orderItemUpdate = orderItem.get().orderItemUpdate(orderItemMapper.dtoSaveToEntity(orderItemDtoSave));
        return orderItemMapper.EntityToDtoSend(orderItemRepository.save(orderItemUpdate));
    }
}
