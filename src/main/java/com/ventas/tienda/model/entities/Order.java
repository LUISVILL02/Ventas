package com.ventas.tienda.model.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    @Column(nullable = false)
    private LocalDateTime oderDate;

    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems;

    @OneToOne
    @JoinColumn(name = "idPayment")
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "idDetail")
    private ShippingDetails details;

    public enum Status {
        PENDING, SENT, DELIVERED
    }
    
}
