package com.ventas.tienda.model.Dtos.send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDtoSend {
    private Long idPayment;
    private Double paymentAmount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private OrderDtoSend order;
}
