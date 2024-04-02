package com.ventas.tienda.model.Dtos.save;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ventas.tienda.model.entities.Order;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoSave {
    @JsonFormat(pattern = "dd/MM/yyyy",timezone = "GMT-5")
    private LocalDateTime oderDate;
    private Order.Status status;
}
