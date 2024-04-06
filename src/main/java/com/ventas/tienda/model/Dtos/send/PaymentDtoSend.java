package com.ventas.tienda.model.Dtos.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDtoSend {
    private Long idPayment;
    private Double paymentTotal;
    private LocalDate paymentDate;
    private String paymentMethod;
    private Long idOrder;
}
