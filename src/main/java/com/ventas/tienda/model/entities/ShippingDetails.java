package com.ventas.tienda.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalles-envios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetail;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String carrier;
    @Column(nullable = false)
    private Long guideNumber;

    @OneToOne(mappedBy = "details")
    private Order order;
}
