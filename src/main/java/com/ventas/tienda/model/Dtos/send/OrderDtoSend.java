package com.ventas.tienda.model.Dtos.send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoSend {
    private Long idOrder;
    private LocalDateTime orderDate;
    private String status;
    private CustomerDtoSend customer;
    List<OrderItemDtoSend> orderItems;
    private ShippingDetailsDtoSend shippingDetails;
    private PaymentDtoSend payment;
}
