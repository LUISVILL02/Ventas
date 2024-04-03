package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.PaymentDtoSave;
import com.ventas.tienda.model.Dtos.send.PaymentDtoSend;
import com.ventas.tienda.model.entities.Payment;
import com.ventas.tienda.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<PaymentDtoSend> paymentDtoSend = paymentService.findById(id);
        if (paymentDtoSend.isEmpty()){
            return new ResponseEntity<>("Payment not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(paymentDtoSend.get());
    }

    @GetMapping
    public ResponseEntity<Page<PaymentDtoSend>> getAll(){
        return ResponseEntity.ok(paymentService.findAll());
    }
    @GetMapping("/order/{id}/{paymentMethod}")
    public ResponseEntity<Page<PaymentDtoSend>> getByOrder(@PathVariable Long id, @PathVariable Payment.MethodPay paymentMethod){
        return ResponseEntity.ok(paymentService.findByPaymentMethodAndOrder_IdOrder(paymentMethod, id));
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<PaymentDtoSend>> getByDateRange(
            @RequestParam
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(pattern = "dd/MM/yyyy")
            LocalDate endDate){
        return ResponseEntity.ok(paymentService.findByPaymentDateBetween(startDate, endDate));
    }

    @PostMapping("{idOrder}")
    public ResponseEntity<PaymentDtoSend> save(@RequestBody PaymentDtoSave paymentDtoSave, @PathVariable Long idOrder){
        return new ResponseEntity<>(paymentService.save(paymentDtoSave, idOrder), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<PaymentDtoSend> update(@RequestBody PaymentDtoSave paymentDtoSave, @PathVariable Long id){
        return new ResponseEntity<>(paymentService.update(paymentDtoSave, id), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        paymentService.deleteById(id);
        return ResponseEntity.ok().body("Payment deleted");
    }
}
