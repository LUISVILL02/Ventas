package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.OrderItemDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderItemDtoSend;
import com.ventas.tienda.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("{id}")
    public ResponseEntity<OrderItemDtoSend> getById(@PathVariable Long id) {
        Optional<OrderItemDtoSend> orderItem = orderItemService.findById(id);
        return orderItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<OrderItemDtoSend>> getAll() {
        return ResponseEntity.ok(orderItemService.findAll());
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderItemDtoSend>> getByOrderId(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.findByOrder_IdOrder(id));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<OrderItemDtoSend>> getByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.findByProduct_IdProduct(id));
    }

    @PostMapping("{idProduct}/{idOrder}")
    public ResponseEntity<OrderItemDtoSend> save(@RequestBody OrderItemDtoSave orderItemDtoSave,
                                                 @PathVariable Long idProduct,
                                                 @PathVariable Long idOrder) {
        return new ResponseEntity<>(orderItemService.save(orderItemDtoSave, idProduct, idOrder), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderItemDtoSend> update(@RequestBody OrderItemDtoSave orderItemDtoSave, @PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.update(orderItemDtoSave, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        orderItemService.deleteById(id);
        return ResponseEntity.ok().body("OrderItem deleted");
    }
}
