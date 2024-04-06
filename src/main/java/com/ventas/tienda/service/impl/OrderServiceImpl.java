package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.Dtos.save.OrderDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.entities.Customer;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.mapper.OrderMapper;
import com.ventas.tienda.repository.CustomerRepository;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl extends ServiceImp<OrderDtoSave, OrderDtoSend, Order> implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final CustomerRepository customerRepository;

    protected OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, CustomerRepository customerRepository) {
        super(orderRepository, orderMapper);
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<OrderDtoSend> findByOderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> listOrder = orderRepository.findByOrderDateBetween(pageable, startDate, endDate);
        return listOrder.map(orderMapper::EntityToDtoSend);
    }
    @Override
    public Page<OrderDtoSend> findByStatusAndCustomer(Order.Status status, Long idCustomer) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> listOrder = orderRepository.findByStatusAndCustomer_IdCustomer(pageable, status, idCustomer);
        return listOrder.map(orderMapper::EntityToDtoSend);
    }

    @Override
    public Page<OrderDtoSend> findByCustomerWithItems(Long idCustomer) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> listOrder = orderRepository.findByCustomerWithItems(pageable, idCustomer);
        return listOrder.map(orderMapper::EntityToDtoSend);
    }

    @Override
    public OrderDtoSend update(OrderDtoSave orderDtoSave, Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            return orderMapper.EntityToDtoSend(
                    orderRepository.save(
                            orderMapper.dtoSaveToEntity(orderDtoSave)));
        }
        Order orderUpdate = order.get().updateOrder(orderMapper.dtoSaveToEntity(orderDtoSave));
        return orderMapper.EntityToDtoSend(orderRepository.save(orderUpdate));
    }

    @Override
    public OrderDtoSend save(OrderDtoSave orderDtoSave, Long idCustomer) {
        Customer customer = customerRepository.findById(idCustomer)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Order order = orderMapper.dtoSaveToEntity(orderDtoSave);
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        customer.getOrders().add(order);
        return orderMapper.EntityToDtoSend(orderRepository.save(order));
    }


}
