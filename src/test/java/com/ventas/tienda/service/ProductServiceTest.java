package com.ventas.tienda.service;

import com.ventas.tienda.model.Dtos.save.ProductDtoSave;
import com.ventas.tienda.model.Dtos.send.ProductDtoSend;
import com.ventas.tienda.model.entities.Product;
import com.ventas.tienda.model.mapper.ProductMapper;
import com.ventas.tienda.repository.ProductRepository;
import com.ventas.tienda.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    Product product;
    ProductDtoSend productDtoSend;
    ProductDtoSave productSave;

    private Page<Product> createPageWithSingleProduct(Product product) {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        return new PageImpl<>(productList);
    }

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .idProduct(1L)
                .price(10.0)
                .stock(10)
                .name("mouse")
                .build();

        productDtoSend = ProductDtoSend.builder()
                .idProduct(1L)
                .price(10.0)
                .stock(10)
                .name("mouse")
                .orderItems(new ArrayList<>())
                .build();
        productSave = ProductDtoSave.builder()
                .price(10.0)
                .stock(10)
                .name("mouse")
                .build();
    }

    @Test
    void givenName_whenFindByName_thenReturnPageProductDtoSend() {
        String name = "mouse";
        Page<Product> page = createPageWithSingleProduct(product);

        when(productMapper.EntityToDtoSend(page.getContent().get(0))).thenReturn(productDtoSend);

        when(productRepository.findByName(PageRequest.of(0, 10), name)).thenReturn(page);

        Page<ProductDtoSend> result = productService.findByName(name);
        assertNotNull(result);
        assertEquals(result.getContent().get(0).getIdProduct(), productDtoSend.getIdProduct());
    }

    @Test
    void givenStock_whenFindByStockGreaterThan_thenReturnPageProductDtoSend() {
        Integer stock = 0;
        Page<Product> page = createPageWithSingleProduct(product);
        when(productRepository.findByStockGreaterThan(PageRequest.of(0, 10), stock)).thenReturn(page);

        when(productMapper.EntityToDtoSend(page.getContent().get(0))).thenReturn(productDtoSend);

        Page<ProductDtoSend> result = productService.findByStockGreaterThan(stock);
        assertNotNull(result);
        assertEquals(result.getContent().get(0).getIdProduct(), productDtoSend.getIdProduct());
    }

    @Test
    void givenStockAndPrice_whenFindByStockAndPriceLessThan_thenReturnPageProductDtoSend() {
        Integer stock = 0;
        Double price = 10.0;
        Page<Product> page = createPageWithSingleProduct(product);
        when(productRepository.findByStockAndPriceLessThan(PageRequest.of(0, 10), stock, price)).thenReturn(page);

        when(productMapper.EntityToDtoSend(page.getContent().get(0))).thenReturn(productDtoSend);

        Page<ProductDtoSend> result = productService.findByStockAndPriceLessThan(stock, price);
        assertNotNull(result);
        assertEquals(result.getContent().get(0).getIdProduct(), productDtoSend.getIdProduct());
    }

    @Test
    void givenIdProduct_whenFindById_thenReturnProductDtoSend() {
        Long id = 1L;
        Optional<Product> optionalProduct = Optional.of(product);
        when(productRepository.findById(id)).thenReturn(optionalProduct);
        when(productMapper.EntityToDtoSend(product)).thenReturn(productDtoSend);
        ProductDtoSend result = productService.findById(id).get();
        assertNotNull(result);
        assertEquals(result.getIdProduct(), product.getIdProduct());
    }

    @Test
    void givenProductDtoSave_whenUpdate_thenReturnProductDtoSend() {
        Long id = 1L;
        Optional<Product> optionalProduct = Optional.of(product);
        when(productRepository.findById(id)).thenReturn(optionalProduct);
        when(productMapper.dtoSaveToEntity(productSave)).thenReturn(product);
        when(productMapper.EntityToDtoSend(product)).thenReturn(productDtoSend);
        when(productRepository.save(product)).thenReturn(product);
        ProductDtoSend result = productService.update(productSave, id);
        assertNotNull(result);
        assertEquals(result.getIdProduct(), product.getIdProduct());
        assertEquals(result.getName(), product.getName());
    }

    @Test
    void givenNotExistingIdProduct_whenUpdate_thenSaveProductDtoSend() {
        Long id = 2L;
        Optional<Product> optionalProduct = Optional.empty();
        when(productRepository.findById(id)).thenReturn(optionalProduct);
        when(productMapper.dtoSaveToEntity(productSave)).thenReturn(product);
        when(productMapper.EntityToDtoSend(product)).thenReturn(productDtoSend);
        when(productRepository.save(product)).thenReturn(product);
        ProductDtoSend result = productService.update(productSave, id);
        assertNotNull(result.getIdProduct());
        assertEquals(result.getIdProduct(), product.getIdProduct());
        assertEquals(result.getName(), product.getName());
    }

    @Test
    void givenProductDtoSave_whenSave_thenReturnProductDtoSend() {
        when(productMapper.dtoSaveToEntity(productSave)).thenReturn(product);
        when(productMapper.EntityToDtoSend(product)).thenReturn(productDtoSend);
        when(productRepository.save(product)).thenReturn(product);
        ProductDtoSend result = productService.save(productSave);
        assertNotNull(result);
        assertEquals(result.getIdProduct(), product.getIdProduct());
        assertEquals(result.getName(), product.getName());
    }

}