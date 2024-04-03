package com.ventas.tienda.model.entities;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCustomer;

    @Column(nullable = false)
    private String name;

    @Column( unique = true)
    private String email;

    @Column( nullable = false)
    private String address;
    
    @OneToMany(mappedBy = "customer")
    List<Order> orders;

    public Customer updateCustomer(Customer customer){
        return new Customer(this.idCustomer, customer.name, customer.email, customer.address, customer.orders);
    }
}
