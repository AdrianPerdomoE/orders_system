package com.adpe.orders_system.model.query;

import com.adpe.orders_system.DTO.CustomQuery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomQueryFactory {

    private QueryStrategy queryStrategy;

    public Object buildQuery(CustomQuery query) {
       return queryStrategy.createQuery(query);
    }
    
    public void setQueryStrategy(QueryStrategy queryStrategy) {
        this.queryStrategy = queryStrategy;
    }

}
