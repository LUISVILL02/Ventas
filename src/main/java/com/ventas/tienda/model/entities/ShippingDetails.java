package com.ventas.tienda.model.entities;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "detalles-envios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToOne
    @JoinColumn(name = "idOrder")
    private Order order;

    public ShippingDetails shippingDetailsUpdate(ShippingDetails shippingDetails) {
        Order orderUpdate = getOrder() != null ? getOrder() : shippingDetails.getOrder();
        return new ShippingDetails(this.idDetail, shippingDetails.address, shippingDetails.carrier, shippingDetails.guideNumber, orderUpdate);
    }
}
