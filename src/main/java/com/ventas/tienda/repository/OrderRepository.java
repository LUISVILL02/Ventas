package com.ventas.tienda.repository;

import com.ventas.tienda.model.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends Repository<Order>{
    Page<Order> findByOderDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
    Page<Order> findByStatusAndCustomer(Pageable pageable, Order.Status status, Long idCustomer);
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems WHERE o.customer.idCustomer = :customer")
    List<Order> findByCustomerWithItems(@Param("customer") Long idCustomer);

}
