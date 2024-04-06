package com.ventas.tienda.repository;

import com.ventas.tienda.IntegrationDbRepositoryTest;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentRepositoryTest extends IntegrationDbRepositoryTest {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Autowired
    PaymentRepositoryTest(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        orderRepository.deleteAll();
    }

    void createPayment() {
        Order order = Order.builder()
                .status(Order.Status.PENDING)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        Order order2 = Order.builder()
                .status(Order.Status.SENT)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(order2);

        Payment payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .paymentMethod(Payment.MethodPay.PSE)
                .order(order)
                .paymentTotal(300.0)
                .build();
        paymentRepository.save(payment);

        Payment payment2 = Payment.builder()
                .paymentDate(LocalDate.now())
                .paymentMethod(Payment.MethodPay.CREDIT_CARD)
                .order(order2)
                .paymentTotal(200.0)
                .build();
        paymentRepository.save(payment2);
    }

    @Test
    void findByPaymentDateBetween() {
        createPayment();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);

        assertEquals(2, paymentRepository.findByPaymentDateBetween(pageable, startDate, endDate).getTotalElements());
    }

    @Test
    void findByPaymentMethodAndOrder_IdOrder() {
        createPayment();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Payment> payments = paymentRepository.findByPaymentMethodAndOrder_IdOrder(pageable, Payment.MethodPay.PSE, 1L);

        assertEquals(1, payments.getTotalElements());
        assertEquals(Payment.MethodPay.PSE, payments.getContent().get(0).getPaymentMethod());
        assertEquals(300.0, payments.getContent().get(0).getPaymentTotal());
    }

    @Test
    void givenPayment_whenSave_thenPaymentId() {
        createPayment();

        Payment savedPayment = paymentRepository.findAll().get(0);

        assertNotNull(savedPayment.getIdPayment());
        assertNotNull(savedPayment.getOrder());
    }

    @Test
    void givenIdPayment_whenFindByIdPayment_thenReturnPayment() {
        createPayment();

        Long idPayment = 1L;

        Optional<Payment> payment = paymentRepository.findById(idPayment);

        assertEquals(Payment.MethodPay.PSE, payment.get().getPaymentMethod());
        assertEquals(300.0, payment.get().getPaymentTotal());
    }

    @Test
    void givenIdPayment_whenDeleteById_thenPaymentDeleted() {
        createPayment();

        Long idPayment = 1L;

        paymentRepository.deleteById(idPayment);

        assertEquals(1, paymentRepository.findAll().size());
    }
}