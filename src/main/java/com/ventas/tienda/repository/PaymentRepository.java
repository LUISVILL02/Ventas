package com.ventas.tienda.repository;

import com.ventas.tienda.model.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PaymentRepository extends Repository<Payment> {
    Page<Payment> findByPaymentDateBetween(Pageable pageable, LocalDate startDate, LocalDate endDate);
    Page<Payment> findByPaymentMethodAndOrder_IdOrder(Pageable pageable, Payment.MethodPay paymentMethod, Long idOrder);
}
