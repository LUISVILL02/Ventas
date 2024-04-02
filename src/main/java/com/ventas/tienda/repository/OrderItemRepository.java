package com.ventas.tienda.repository;

import com.ventas.tienda.model.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends Repository<OrderItem> {
    List<OrderItem> findByOrder_IdOrder(Long idOrder);
    List<OrderItem> findByProduct_IdProduct(Long idProduct);
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.idProduct = :idProduct")
    Double sumTotalSalesByProduct(@Param("idProduct") Long idProduct);
}
