package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.PaymentDtoSave;
import com.ventas.tienda.model.Dtos.send.PaymentDtoSend;
import com.ventas.tienda.model.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PaymentService extends Service<PaymentDtoSave, PaymentDtoSend, Payment>{
    Page<PaymentDtoSend> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    Page<PaymentDtoSend> findByPaymentMethodAndOrder_IdOrder(Payment.MethodPay paymentMethod, Long idOrder);
    PaymentDtoSend save(PaymentDtoSave paymentDtoSave, Long idOrder);
    PaymentDtoSend update(PaymentDtoSave paymentDtoSave, Long id);
}