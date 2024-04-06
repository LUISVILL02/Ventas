package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.PaymentDtoSave;
import com.ventas.tienda.model.Dtos.send.PaymentDtoSend;
import com.ventas.tienda.model.entities.Customer;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.Payment;
import com.ventas.tienda.model.mapper.PaymentMapper;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.repository.PaymentRepository;
import com.ventas.tienda.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    Payment payment;
    PaymentDtoSave paymentDtoSave;
    PaymentDtoSend paymentDtoSend;
    Order order;

    private Page<Payment> createPageWithSinglePayment(Payment payment) {
        List<Payment> paymentsList = new ArrayList<>();
        paymentsList.add(payment);
        return new PageImpl<>(paymentsList);
    }

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .idOrder(1L)
                .payment(payment)
                .build();
        payment = Payment.builder()
                .idPayment(1L)
                .paymentDate(LocalDate.now())
                .paymentMethod(Payment.MethodPay.CREDIT_CARD)
                .order(order)
                .build();
        paymentDtoSave = PaymentDtoSave.builder()
                .paymentDate(LocalDate.now())
                .paymentMethod("CREDIT_CARD")
                .build();
        paymentDtoSend = PaymentDtoSend.builder()
                .idPayment(1L)
                .paymentDate(LocalDate.now())
                .paymentMethod("CREDIT_CARD")
                .idOrder(1L)
                .build();
    }

    @Test
    void givenEndDateAndStartDate_whenFindByPaymentDateBetween_thenReturnPagePaymentDtoSend() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        Page<Payment> pagePayment = createPageWithSinglePayment(payment);

        when(paymentRepository.findByPaymentDateBetween(PageRequest.of(0, 10), startDate, endDate)).thenReturn(pagePayment);
        when(paymentMapper.EntityToDtoSend(payment)).thenReturn(paymentDtoSend);

        Page<PaymentDtoSend> result = paymentService.findByPaymentDateBetween(startDate, endDate);

        assertEquals(1, result.getContent().size());
        assertEquals(paymentDtoSend, result.getContent().get(0));
    }

    @Test
    void givenPaymentMethodAndIdOrder_whenFindByPaymentMethodAndOrder_IdOrder_thenReturnPagePaymentDtoSend() {
        Page<Payment> pagePayment = createPageWithSinglePayment(payment);

        when(paymentRepository.findByPaymentMethodAndOrder_IdOrder(PageRequest.of(0, 10), Payment.MethodPay.CREDIT_CARD, 1L)).thenReturn(pagePayment);
        when(paymentMapper.EntityToDtoSend(payment)).thenReturn(paymentDtoSend);

        Page<PaymentDtoSend> result = paymentService.findByPaymentMethodAndOrder_IdOrder(Payment.MethodPay.CREDIT_CARD, 1L);

        assertEquals(1, result.getContent().size());
        assertEquals(paymentDtoSend, result.getContent().get(0));
    }

    @Test
    void givenPaymentDtoSaveAndIdOrder_whenSave_thenReturnPaymentDtoSend() {

        when(orderRepository.findById(order.getIdOrder())).thenReturn(Optional.of(order));
        when(paymentMapper.dtoSaveToEntity(paymentDtoSave)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.EntityToDtoSend(payment)).thenReturn(paymentDtoSend);

        PaymentDtoSend result = paymentService.save(paymentDtoSave, order.getIdOrder());

        assertEquals(paymentDtoSend, result);
        assertEquals(order.getPayment(), payment);
        assertEquals(payment.getIdPayment(), paymentDtoSend.getIdPayment());
    }
}