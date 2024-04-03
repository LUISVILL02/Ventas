package com.ventas.tienda.model.mapper;

import com.ventas.tienda.model.Dtos.save.PaymentDtoSave;
import com.ventas.tienda.model.Dtos.send.PaymentDtoSend;
import com.ventas.tienda.model.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends MapperGen<PaymentDtoSave, PaymentDtoSend, Payment>{
    PaymentMapper INSTANCE = Mappers.getMapper( PaymentMapper.class );
    @Mapping( source = "order.idOrder", target = "idOrder")
    PaymentDtoSend EntityToDtoSend(Payment payment);

}
