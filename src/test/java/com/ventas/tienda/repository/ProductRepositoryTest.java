package com.ventas.tienda.repository;

import com.ventas.tienda.IntegrationDbRepositoryTest;
import com.ventas.tienda.model.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest extends IntegrationDbRepositoryTest {

    private final ProductRepository productRepository;

    @Autowired
    ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    void mockData() {
        Product product = Product.builder()
                .name("Product")
                .stock(10)
                .price(100.0)
                .stock(10)
                .build();

        productRepository.save(product);

        Product product2 = Product.builder()
                .name("Product2")
                .stock(0)
                .stock(20)
                .price(200.0)
                .build();
        productRepository.save(product2);

        productRepository.flush();
    }

    @Test
    void givenProduct_whenSave_thenProductId() {
        mockData();
        Product product = productRepository.findAll().get(0);
        assertNotNull(product.getIdProduct());
    }

    @Test
    void givenName_whenFindByName_thenProduct() {
        mockData();
        Pageable pageable = PageRequest.of(0, 10);

        List<Product> products = productRepository.findAll();
        Product product = productRepository.findByName(pageable,"Product").getContent().get(0);

        assertNotNull(product);
        assertEquals(products.get(0).getName(), product.getName());
    }

    @Test
    void givenStock_whenFindByStockGreaterThan_thenProduct() {
        mockData();
        Pageable pageable = PageRequest.of(0, 10);

        List<Product> products = productRepository.findAll();
        Product product = productRepository.findByStockGreaterThan(pageable, 0).getContent().get(0);

        assertNotNull(product);
        assertEquals(products.get(0).getName(), product.getName());
    }

    @Test
    void givenStock_whenFindByStockGreaterThan_thenEmpty() {
        mockData();
        Pageable pageable = PageRequest.of(0, 10);

        List<Product> product = productRepository.findByStockGreaterThan(pageable, 100).getContent();

        assertThat(product).isEmpty();
    }

    @Test
    void givenStockAndPrice_whenFindByStockAndPriceLessThan_thenProduct() {
        mockData();
        Pageable pageable = PageRequest.of(0, 10);

        List<Product> products = productRepository.findAll();
        Product product = productRepository.findByStockAndPriceLessThan(pageable, 10, 200.0).getContent().get(0);

        assertNotNull(product);
        assertEquals(products.get(0).getName(), product.getName());
    }

    @Test
    void givenId_whenFindById_thenProduct() {
        mockData();
        Product product = productRepository.findAll().get(0);
        Product productFind = productRepository.findById(product.getIdProduct()).get();

        assertNotNull(productFind);
        assertEquals(product.getIdProduct(), productFind.getIdProduct());
    }

    @Test
    void givenId_whenFindById_thenEmpty() {
        mockData();

        Optional<Product> productFind = productRepository.findById(3L);

        assertEquals(Optional.empty(), productFind);
    }

    @Test
    void givenIdProduct_whenDeleteById_thenProduct(){
        mockData();
        Long idProduct = 1L;

        productRepository.deleteById(idProduct);

        assertEquals(1, productRepository.findAll().size());
        assertThat(productRepository.findById(idProduct)).isEmpty();
    }
}