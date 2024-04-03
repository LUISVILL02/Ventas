package com.ventas.tienda.model.Dtos.save;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDtoSave {
    @NotBlank
    private String name;
    @Email
    private String email;
    private String address;
}
