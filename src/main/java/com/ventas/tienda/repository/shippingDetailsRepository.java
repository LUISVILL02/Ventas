package com.ventas.tienda.repository;

import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface shippingDetailsRepository extends Repository<ShippingDetails> {
    List<ShippingDetails> findByOrder_IdOrder(Long idOrder);
    List<ShippingDetails> findByCarrier(String carrier);
    List<ShippingDetails> findByOrder_Status(Order.Status status);
}
