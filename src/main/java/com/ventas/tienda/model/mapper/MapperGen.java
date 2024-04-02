package com.ventas.tienda.model.mapper;

import java.util.List;

public interface MapperGen<S, M, E> {
    S EntityToDtoSave(E e);
    E dtoSaveToEntity(S s);
    M EntityToDtoSend(E e);
    E dtoSendToEntity(M m);
    List<M> ListEntityToDtoSend(List<E> e);
    List<E> ListDtoSendToEntity(List<M> m);
}
