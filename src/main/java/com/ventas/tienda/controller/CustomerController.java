package com.ventas.tienda.controller;

import com.ventas.tienda.model.Dtos.save.CustomerDtoSave;
import com.ventas.tienda.model.Dtos.send.CustomerDtoSend;
import com.ventas.tienda.service.CustomerService;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("{id}")
    public ResponseEntity<CustomerDtoSend> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.findById(id).get());
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDtoSend>> getAll(){
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable @Email String email){
        Optional<CustomerDtoSend> customerDtoSend = customerService.findByEmail(email);
        if (customerDtoSend.isEmpty()){
            return ResponseEntity.badRequest().body("Customer not found");
        }
        return ResponseEntity.ok(customerDtoSend.get());
    }

    @GetMapping("/address")
    public ResponseEntity<?> getByAddress(@RequestParam String address){
        Optional<CustomerDtoSend> customerDtoSend = customerService.findByAddress(address);
        if (customerDtoSend.isEmpty()){
            return ResponseEntity.badRequest().body("Customer not found");
        }
        return ResponseEntity.ok(customerDtoSend.get());
    }

    @PostMapping
    public ResponseEntity<CustomerDtoSend> save(@RequestBody CustomerDtoSave customerDtoSave){
        return ResponseEntity.ok(customerService.save(customerDtoSave));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDtoSend> update(@RequestBody CustomerDtoSave customerDtoSave, @PathVariable Long id){
        return ResponseEntity.ok(customerService.update(customerDtoSave, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        customerService.deleteById(id);
        return ResponseEntity.ok().body("Customer deleted");
    }
}
