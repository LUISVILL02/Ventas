package com.ventas.tienda.model.mapper;

import com.ventas.tienda.model.Dtos.save.ShippingDetailsDtoSave;
import com.ventas.tienda.model.Dtos.send.ShippingDetailsDtoSend;
import com.ventas.tienda.model.entities.ShippingDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingDetailsMapper extends MapperGen<ShippingDetailsDtoSave, ShippingDetailsDtoSend, ShippingDetails>{
    @Mapping(source = "order.idOrder", target = "idOrder")
    ShippingDetailsDtoSend EntityToDtoSend(ShippingDetails entity);
}
