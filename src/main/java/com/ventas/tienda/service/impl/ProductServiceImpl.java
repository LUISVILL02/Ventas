package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.Dtos.save.ProductDtoSave;
import com.ventas.tienda.model.Dtos.send.ProductDtoSend;
import com.ventas.tienda.model.entities.Product;
import com.ventas.tienda.model.mapper.ProductMapper;
import com.ventas.tienda.repository.ProductRepository;
import com.ventas.tienda.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImp<ProductDtoSave, ProductDtoSend, Product> implements ProductService{
    protected ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        super(productRepository, productMapper);
    }

    @Override
    public Page<ProductDtoSend> findByName(String name) {
        return null;
    }

    @Override
    public Page<ProductDtoSend> findByStockGreaterThan(Integer stock) {
        return null;
    }

    @Override
    public Page<ProductDtoSend> findByStockAndPriceLessThan(Integer stock, Double price) {
        return null;
    }
}
