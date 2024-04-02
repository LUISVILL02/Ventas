package com.ventas.tienda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ventas.tienda.model.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    Page<Product> findByName(Pageable pageable, String name);
    Page<Product> findByStockGreaterThan(Pageable pageable, Integer stock);
    Page<Product> findByStockLessThanAndPriceLessThan(Pageable pageable, Integer stock, Double price);
}
