package com.ventas.tienda.model.mapper;

import com.ventas.tienda.model.Dtos.save.OrderDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderMapper extends MapperGen<OrderDtoSave, OrderDtoSend, Order>{
    @Mapping( source = "customer.idCustomer", target = "idCustomer")
    OrderDtoSend EntityToDtoSend(Order order);
}
