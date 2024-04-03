package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.OrderDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends Service<OrderDtoSave, OrderDtoSend, Order>{
    Page<OrderDtoSend> findByOderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<OrderDtoSend> findByStatusAndCustomer(Order.Status status, Long idCustomer);
    Page<OrderDtoSend> findByCustomerWithItems(Long idCustomer);
    OrderDtoSend update(OrderDtoSave orderDtoSave, Long id);
    OrderDtoSend save(OrderDtoSave orderDtoSave, Long idCustomer);
}
