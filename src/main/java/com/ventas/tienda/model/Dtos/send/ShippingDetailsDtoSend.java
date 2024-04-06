package com.ventas.tienda.model.Dtos.send;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingDetailsDtoSend {
    private Long idDetail;
    private String address;
    private String carrier;
    private Long guideNumber;
    private Long idOrder;
}
