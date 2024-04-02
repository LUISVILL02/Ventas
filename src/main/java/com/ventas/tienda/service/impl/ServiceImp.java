package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.mapper.MapperGen;
import com.ventas.tienda.repository.Repository;
import com.ventas.tienda.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public abstract class ServiceImp<S, M, E> implements Service<S, M, E> {
    private final Repository<E> repository;
    private final MapperGen<S, M, E> mapper;

    protected ServiceImp(Repository<E> repository, MapperGen<S, M, E> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<M> findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<E> listUser = repository.findAll(pageable);
        return listUser.map(mapper::EntityToDtoSend);
    }

    public Optional<M> findById(Long id) {
        Optional<E> user = Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
        return user.map(mapper::EntityToDtoSend);
    }

    public M save(S s) {
        E e = mapper.dtoSaveToEntity(s);
        return mapper.EntityToDtoSend((repository.save(e)));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public M update(S s, Long id) {
        repository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        E eUpdate = mapper.dtoSaveToEntity(s);
        return mapper.EntityToDtoSend(repository.save(eUpdate));
    }
}
