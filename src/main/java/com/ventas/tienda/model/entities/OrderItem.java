package com.ventas.tienda.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item-pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrderItem;

    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "idOrder")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    public OrderItem orderItemUpdate(OrderItem orderItem){
        Product product = orderItem.getProduct() != null ? orderItem.getProduct() : this.product;
        Order updatedOrder = orderItem.getOrder() != null ? orderItem.getOrder() : this.order;
        return new OrderItem(this.idOrderItem, orderItem.quantity, orderItem.unitPrice, updatedOrder, product);
    }
    
}
