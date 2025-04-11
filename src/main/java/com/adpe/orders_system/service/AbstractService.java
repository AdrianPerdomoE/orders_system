package com.adpe.orders_system.service;

import java.util.List;

import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.repository.AbstractRepository;

public  abstract class AbstractService<T> {
    protected abstract AbstractRepository<T> getRepository();
    public abstract T create(T entity);
    public abstract T getById(String id);
    public abstract T getOne(CustomQuery query);
    public abstract List<T> getMany(CustomQuery query);
    public abstract T updateOne(String id, T entity);
    public abstract void deleteOne(String id);
}
