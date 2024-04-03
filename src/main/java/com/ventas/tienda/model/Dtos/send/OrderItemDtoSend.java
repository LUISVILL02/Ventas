package com.ventas.tienda.model.Dtos.send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDtoSend {
    private Long idOrderItem;

    private Integer quantity;

    private Double unitPrice;
    private Long idOrder;
    private Long idProduct;
}
