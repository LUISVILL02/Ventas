package com.ventas.tienda.model.Dtos.send;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ventas.tienda.model.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDtoSend {
    private Long idCustomer;
    private String name;
    private String email;
    private String address;
    List<OrderDtoSend> orders;
}
