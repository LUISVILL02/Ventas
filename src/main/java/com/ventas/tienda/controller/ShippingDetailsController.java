package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.ShippingDetailsDtoSave;
import com.ventas.tienda.model.Dtos.send.ShippingDetailsDtoSend;
import com.ventas.tienda.service.ShippingDetailsService;
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
@RequestMapping("/api/v1/shipping-details")
public class ShippingDetailsController {
    private final ShippingDetailsService shippingDetailsService;

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<ShippingDetailsDtoSend> shippingDetails = shippingDetailsService.findById(id);
        if (shippingDetails.isEmpty())
            return new ResponseEntity<>("Shipping details not found", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(shippingDetails.get());
    }

    @GetMapping
    public ResponseEntity<Page<ShippingDetailsDtoSend>> getAll() {
        return ResponseEntity.ok(shippingDetailsService.findAll());
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getByOrderId(@PathVariable Long id) {
        List<ShippingDetailsDtoSend> shippingDetails = shippingDetailsService.findByOrder_IdOrder(id);
        if (shippingDetails.isEmpty())
            return new ResponseEntity<>("Shipping details not found", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(shippingDetails);
    }

    @GetMapping("/carrier")
    public ResponseEntity<?> getByCarrier(@RequestParam String nameCarrier) {
        List<ShippingDetailsDtoSend> shippingDetails = shippingDetailsService.findByCarrier(nameCarrier);
        if (shippingDetails.isEmpty())
            return new ResponseEntity<>("Shipping details not found", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(shippingDetails);
    }

    @PostMapping("{idOrder}")
    public ResponseEntity<?> save(@RequestBody ShippingDetailsDtoSave shippingDetailsDtoSave, @PathVariable Long idOrder) {
        return new ResponseEntity<>(shippingDetailsService.save(shippingDetailsDtoSave, idOrder), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@RequestBody ShippingDetailsDtoSave shippingDetailsDtoSave, @PathVariable Long id) {
        return ResponseEntity.ok(shippingDetailsService.update(shippingDetailsDtoSave, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        shippingDetailsService.deleteById(id);
        return ResponseEntity.ok().body("Shipping details deleted");
    }
}
