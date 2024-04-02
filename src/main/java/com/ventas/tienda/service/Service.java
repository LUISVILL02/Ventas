package com.ventas.tienda.service;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface Service<S, M, E>{
    Page<M> findAll();
    Optional<M> findById(Long id);
    M save(S s);
    void deleteById(Long id);
    M update(S s, Long id);
}
