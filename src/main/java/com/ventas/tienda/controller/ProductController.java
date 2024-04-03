package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.ProductDtoSave;
import com.ventas.tienda.model.Dtos.send.ProductDtoSend;
import com.ventas.tienda.service.ProductService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{idProduct}")
    public ResponseEntity<ProductDtoSend> getById(@PathVariable Long idProduct){
        return ResponseEntity.ok(productService.findById(idProduct).get());
    }

    @GetMapping("/name")
    public ResponseEntity<Page<ProductDtoSend>> getByName(@RequestParam @NotBlank String name){
        return ResponseEntity.ok(productService.findByName(name));
    }

    @GetMapping("/stock")
    public ResponseEntity<Page<ProductDtoSend>> getByStockGreaterThan(){
        Integer stock = 0;
        return ResponseEntity.ok(productService.findByStockGreaterThan(stock));
    }

    @PostMapping
    public ResponseEntity<ProductDtoSend> save(@RequestBody ProductDtoSave productDtoSave){
        return ResponseEntity.ok(productService.save(productDtoSave));
    }

    @PutMapping("/{idProduct}")
    public ResponseEntity<ProductDtoSend> update(@RequestBody ProductDtoSave productDtoSave, @PathVariable Long idProduct){
        return ResponseEntity.ok(productService.update(productDtoSave, idProduct));
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<String> delete(@PathVariable Long idProduct){
        productService.deleteById(idProduct);
        return ResponseEntity.ok().body("Product deleted");
    }
}
