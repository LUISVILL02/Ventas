package com.ventas.tienda.model.Dtos.send;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDtoSend {
    private Long idOrder;
    private LocalDateTime orderDate;
    private String status;
    private Long idCustomer;
    List<OrderItemDtoSend> orderItems;
    private ShippingDetailsDtoSend shippingDetails;
    private PaymentDtoSend payment;
}
