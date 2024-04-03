package com.ventas.tienda.model.mapper;

import com.ventas.tienda.model.Dtos.save.OrderItemDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.Dtos.send.OrderItemDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderItemMapper extends MapperGen<OrderItemDtoSave, OrderItemDtoSend, OrderItem>{
    @Mappings({
            @Mapping(source = "order.idOrder", target = "idOrder"),
            @Mapping(source = "product.idProduct", target = "idProduct")
    })
    OrderItemDtoSend EntityToDtoSend(OrderItem orderItem);
}
