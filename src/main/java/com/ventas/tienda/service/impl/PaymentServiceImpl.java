package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.Dtos.save.PaymentDtoSave;
import com.ventas.tienda.model.Dtos.send.PaymentDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.Payment;
import com.ventas.tienda.model.mapper.PaymentMapper;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.repository.PaymentRepository;
import com.ventas.tienda.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PaymentServiceImpl extends ServiceImp<PaymentDtoSave, PaymentDtoSend, Payment> implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository  orderRepository;
    private final PaymentMapper paymentMapper;
    protected PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository, PaymentMapper paymentMapper) {
        super(paymentRepository, paymentMapper);
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Page<PaymentDtoSend> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> listPayment = paymentRepository.findByPaymentDateBetween(pageable, startDate, endDate);
        return listPayment.map(paymentMapper::EntityToDtoSend);
    }

    @Override
    public Page<PaymentDtoSend> findByPaymentMethodAndOrder_IdOrder(Payment.MethodPay paymentMethod, Long idOrder) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> listPayment = paymentRepository.findByPaymentMethodAndOrder_IdOrder(pageable, paymentMethod, idOrder);
        return listPayment.map(paymentMapper::EntityToDtoSend);
    }

    @Override
    public PaymentDtoSend save(PaymentDtoSave paymentDtoSave, Long idOrder) {
        Optional<Order> order = orderRepository.findById(idOrder);
        Payment payment = paymentMapper.dtoSaveToEntity(paymentDtoSave);
        payment.setOrder(order.get());
        order.get().setPayment(payment);
        return paymentMapper.EntityToDtoSend(paymentRepository.save(payment));
    }

    @Override
    public PaymentDtoSend update(PaymentDtoSave paymentDtoSave, Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isEmpty()) {
            return paymentMapper.EntityToDtoSend(
                    paymentRepository.save(
                            paymentMapper.dtoSaveToEntity(paymentDtoSave)));
        }
        Payment paymentUpdate = payment.get().paymentUpdate(paymentMapper.dtoSaveToEntity(paymentDtoSave));
        return paymentMapper.EntityToDtoSend(paymentRepository.save(paymentUpdate));
    }
}
