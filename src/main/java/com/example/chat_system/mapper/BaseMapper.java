package com.example.chat_system.mapper;


import java.util.List;

public interface BaseMapper <E,D>{

    public E toEntity(D dto);
    public D toDTO(E entity);
    public List<D> toDTO(List<E> eList);



}
