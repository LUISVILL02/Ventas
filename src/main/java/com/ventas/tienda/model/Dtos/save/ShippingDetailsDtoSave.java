package com.ventas.tienda.model.Dtos.save;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetailsDtoSave {
    private String address;
    private String carrier;
    private Integer guideNumber;
}
