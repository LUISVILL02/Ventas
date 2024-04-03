package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.Dtos.save.ProductDtoSave;
import com.ventas.tienda.model.Dtos.send.ProductDtoSend;
import com.ventas.tienda.model.entities.Product;
import com.ventas.tienda.model.mapper.ProductMapper;
import com.ventas.tienda.repository.ProductRepository;
import com.ventas.tienda.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl extends ServiceImp<ProductDtoSave, ProductDtoSend, Product> implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    protected ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        super(productRepository, productMapper);
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Page<ProductDtoSend> findByName(String name) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> listProduct = productRepository.findByName(pageable, name);
        return listProduct.map(productMapper::EntityToDtoSend);
    }

    @Override
    public Page<ProductDtoSend> findByStockGreaterThan(Integer stock) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> listProduct = productRepository.findByStockGreaterThan(pageable, stock);
        return listProduct.map(productMapper::EntityToDtoSend);
    }

    @Override
    public Page<ProductDtoSend> findByStockAndPriceLessThan(Integer stock, Double price) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> listProduct = productRepository.findByStockAndPriceLessThan(pageable, stock, price);
        return listProduct.map(productMapper::EntityToDtoSend);
    }

    @Override
    public ProductDtoSend update(ProductDtoSave productDtoSave, Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return productMapper.EntityToDtoSend(
                    productRepository.save(
                            productMapper.dtoSaveToEntity(productDtoSave)));
        }
        Product productUpdate = product.get().updateProduct(productMapper.dtoSaveToEntity(productDtoSave));
        return productMapper.EntityToDtoSend(productRepository.save(productUpdate));
    }
}
