package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.OrderDtoSave;
import com.ventas.tienda.model.Dtos.save.OrderItemDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.model.Dtos.send.OrderItemDtoSend;
import com.ventas.tienda.model.entities.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemService extends Service<OrderItemDtoSave, OrderItemDtoSend, OrderItem>{
    List<OrderItemDtoSend> findByOrder_IdOrder(Long idOrder);
    List<OrderItemDtoSend> findByProduct_IdProduct(Long idProduct);
    Double sumTotalSalesByProduct(Long idProduct);
    OrderItemDtoSend save(OrderItemDtoSave orderItemDtoSave, Long idOProduct, Long idOrder);
    OrderItemDtoSend update(OrderItemDtoSave orderItemDtoSave, Long id);
}
