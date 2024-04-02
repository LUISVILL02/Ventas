package com.ventas.tienda.model.Dtos.save;

import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.Dtos.send.ProductDtoSend;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDtoSave {
    @Min(1)
    private Integer quantity;
    private Double unitPrice;
}
