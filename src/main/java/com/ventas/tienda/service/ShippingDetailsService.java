
package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.ShippingDetailsDtoSave;
import com.ventas.tienda.model.Dtos.send.ShippingDetailsDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.ShippingDetails;

import java.util.List;

public interface ShippingDetailsService extends Service<ShippingDetailsDtoSave, ShippingDetailsDtoSend, ShippingDetails>{
    List<ShippingDetailsDtoSend> findByOrder_IdOrder(Long idOrder);
    List<ShippingDetailsDtoSend> findByCarrier(String carrier);
    List<ShippingDetailsDtoSend> findByOrder_Status(Order.Status status);
    ShippingDetailsDtoSend save(ShippingDetailsDtoSave shippingDetailsDtoSave, Long idOrder);
    ShippingDetailsDtoSend update(ShippingDetailsDtoSave shippingDetailsDtoSave, Long id);
}
