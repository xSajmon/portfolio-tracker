package com.simon.portfoliotracker.base;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMapper<E, D>{

    public abstract D mapToDto(E entity);
    public abstract E mapToEntity(D dto);

    public List<D> mapToDtos(List<E> sourceList){
        return sourceList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<E> mapToEntities(List<D> dtoList){
        return dtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}
