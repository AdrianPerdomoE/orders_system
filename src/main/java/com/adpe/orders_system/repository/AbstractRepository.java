package com.adpe.orders_system.repository;
import java.util.List;

import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.model.query.CustomQueryFactory;

public abstract class AbstractRepository<T> {

    protected abstract String getCollectionName();  // Nombre de la colección/tabla
    protected abstract CustomQueryFactory getQueryBuilder();  // Builder de queries

    public abstract T create(T entity);
    public abstract T getById(String id);
    public abstract T getOne(CustomQuery query);
    public abstract List<T> getMany(CustomQuery query);
    public abstract T updateOne(String id, T entity);
    public abstract boolean deleteOne(String id);
}