package com.ventas.tienda.model.Dtos.send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDtoSend {
    private Long idPayment;
    private Double paymentTotal;
    private LocalDate paymentDate;
    private String paymentMethod;
    private Long idOrder;
}
