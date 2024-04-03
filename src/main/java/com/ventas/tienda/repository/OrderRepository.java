package com.ventas.tienda.repository;

import com.ventas.tienda.model.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends Repository<Order>{
    Page<Order> findByOrderDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
    Page<Order> findByStatusAndCustomer(Pageable pageable, Order.Status status, Long idCustomer);
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems WHERE o.customer.idCustomer = :customer")
    Page<Order> findByCustomerWithItems(Pageable pageable, @Param("customer") Long idCustomer);

}
