package com.adpe.orders_system.model.query;

import com.adpe.orders_system.DTO.CustomQuery;

public class MongoQueryStrategy extends QueryStrategy {
    
    @Override
    public Object createQuery(CustomQuery query) {
        MongoQueryBuilder queryBuilder = new MongoQueryBuilder();
        queryBuilder.withFilters(query.getProperties())
                    .withOrder(query.getOrderBy(), query.getOrderDirection())
                    .withLimit(query.getLimit())
                    .withOffset(query.getOffset());
        return queryBuilder.build();
    }

}
