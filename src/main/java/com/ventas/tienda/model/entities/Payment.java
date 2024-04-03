package com.ventas.tienda.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayment;
    @Column(name = "payment_total")
    private Double paymentTotal;
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    @Column(name = "payment_method")
    private MethodPay paymentMethod;

    @OneToOne
    @JoinColumn(name = "idOrder")
    private Order order;

    public enum MethodPay {
        CASH, CREDIT_CARD, PAYPAL, NEQUI, DAVIPLATA, PSE
    }

    public Payment paymentUpdate(Payment payment){
        Order updatedOrder = payment.getOrder() != null ? payment.getOrder() : this.order;
        return new Payment(this.idPayment, payment.paymentTotal, payment.paymentDate, payment.paymentMethod, updatedOrder);
    }
}
