package com.ventas.tienda.model.mapper;

import com.ventas.tienda.model.Dtos.save.CustomerDtoSave;
import com.ventas.tienda.model.Dtos.send.CustomerDtoSend;
import com.ventas.tienda.model.entities.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends MapperGen<CustomerDtoSave, CustomerDtoSend, Customer>{
}
