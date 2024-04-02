package com.ventas.tienda.model.Dtos.save;

import com.ventas.tienda.model.Dtos.send.OrderItemDtoSend;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoSave {
    @NotBlank
    private String name;
    @Min(0)
    private Double price;
    @Min(0)
    private Integer stock;
}
