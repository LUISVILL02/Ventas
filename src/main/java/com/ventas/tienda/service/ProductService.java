package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.ProductDtoSave;
import com.ventas.tienda.model.Dtos.send.ProductDtoSend;
import com.ventas.tienda.model.entities.Product;
import org.springframework.data.domain.Page;

public interface ProductService extends Service<ProductDtoSave, ProductDtoSend, Product>{
    Page<ProductDtoSend> findByName(String name);
    Page<ProductDtoSend> findByStockGreaterThan(Integer stock);
    Page<ProductDtoSend> findByStockAndPriceLessThan(Integer stock, Double price);
}
