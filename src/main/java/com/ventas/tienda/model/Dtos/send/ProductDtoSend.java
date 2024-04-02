package com.ventas.tienda.model.Dtos.send;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoSend {
    private Long idProduct;
    private String name;
    private Double price;
    private Integer stock;
    @JsonIgnore
    private List<OrderItemDtoSend> orderItems;
}
