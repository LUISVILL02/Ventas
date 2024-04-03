package com.ventas.tienda.model.entities;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
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


@Entity
@Table(name = "pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToOne(mappedBy = "order")
    private ShippingDetails details;

    public enum Status {
        PENDING, SENT, DELIVERED
    }

    public Order updateOrder(Order order){
        Customer updatedCustomer = order.getCustomer() != null ? order.getCustomer() : this.customer;
        return new Order(this.idOrder, LocalDateTime.now(), order.status, updatedCustomer, order.orderItems, order.payment, order.details);
    }
    
}
