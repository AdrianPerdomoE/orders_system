package com.adpe.orders_system.model.query;
import com.adpe.orders_system.DTO.CustomQuery;



public abstract class QueryStrategy {
    public abstract Object createQuery(CustomQuery query);
}

