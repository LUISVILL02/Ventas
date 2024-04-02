package com.ventas.tienda.model.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private LocalDateTime paymentDate;
    @Column(name = "payment_method")
    private MethodPay paymentMethod;

    @OneToOne(mappedBy = "payment")
    private Order order;

    public enum MethodPay {
        CASH, CREDIT_CARD, PAYPAL, NEQUI, DAVIPLATA, PSE
    }
}
