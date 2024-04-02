package com.ventas.tienda.repository;

import com.ventas.tienda.model.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PaymentRepository extends Repository<Payment> {
    Page<Payment> findByPaymentDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
    Page<Payment> findByPaymentMethodAndOrder_IdOrder(Pageable pageable, Payment.MethodPay paymentMethod, Long idOrder);
}
