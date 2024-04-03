package com.ventas.tienda.model.Dtos.save;

import com.ventas.tienda.model.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoSave {
    private Order.Status status;
}
