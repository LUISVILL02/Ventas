package com.ventas.tienda.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ventas.tienda.model.Dtos.save.OrderDtoSave;
import com.ventas.tienda.model.Dtos.send.OrderDtoSend;
import com.ventas.tienda.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<OrderDtoSend> orderDtoSend = orderService.findById(id);
        if (orderDtoSend.isEmpty()){
            return ResponseEntity.badRequest().body("Order not found");
        }
        return ResponseEntity.ok(orderDtoSend.get());
    }

    @GetMapping
    public ResponseEntity<Page<OrderDtoSend>> getAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<Page<OrderDtoSend>> getByCustomer(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findByCustomerWithItems(id));
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<OrderDtoSend>> getByDateRange(
                                        @RequestParam
                                        @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                        LocalDateTime startDate,
                                        @RequestParam
                                        @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                        LocalDateTime endDate){
        return ResponseEntity.ok(orderService.findByOderDateBetween(startDate, endDate));
    }

    @PostMapping("{idCustomer}")
    public ResponseEntity<OrderDtoSend> save(@RequestBody OrderDtoSave orderDtoSave, @PathVariable Long idCustomer){
        return new ResponseEntity<>(orderService.save(orderDtoSave, idCustomer), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderDtoSend> update(@RequestBody OrderDtoSave orderDtoSave, @PathVariable Long id){
        return new ResponseEntity<>(orderService.update(orderDtoSave, id), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        orderService.deleteById(id);
        return ResponseEntity.ok("Order delete");
    }
}
