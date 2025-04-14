package com.adpe.orders_system.model.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoQueryBuilder {

    private final Query mongoQuery;

    public MongoQueryBuilder() {
        this.mongoQuery = new Query();
    }

    public MongoQueryBuilder withFilters(List<QueryProperty> properties) {
        if (properties == null || properties.isEmpty()) return this;

        List<Criteria> criteriaList = new ArrayList<>();
        for (QueryProperty prop : properties) {
            String field = prop.getField();
            Object value = prop.getValue();
            switch (prop.getOperator()) {
                
                case "equals" -> criteriaList.add(Criteria.where(field).is(value));
                case "not_equals" -> criteriaList.add(Criteria.where(field).ne(value));
                case "greater_than" -> criteriaList.add(Criteria.where(field).gt(value));
                case "less_than" -> criteriaList.add(Criteria.where(field).lt(value));
                case "in" -> criteriaList.add(Criteria.where(field).in((List<?>) value));
                case "not_in" -> criteriaList.add(Criteria.where(field).nin((List<?>) value));
                case "like" -> criteriaList.add(Criteria.where(field).regex(value.toString(), "i"));
                
            }
        }

        mongoQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        return this;
    }

    public MongoQueryBuilder withOrder(String field, String direction) {
        if (field != null) {
            direction = direction == null ? "asc" : direction.toLowerCase();
            Sort.Direction sortDir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            mongoQuery.with(Sort.by(sortDir, field));
        }
        return this;
    }

    public MongoQueryBuilder withLimit(int limit) {
        if (limit > 0) mongoQuery.limit(limit);
        return this;
    }

    public MongoQueryBuilder withOffset(int offset) {
        if (offset > 0) mongoQuery.skip(offset);
        return this;
    }

    public Query build() {
        if (mongoQuery.getQueryObject().isEmpty()) {
            // Agregar un criterio vacío explícito para indicar "seleccionar todo"
            mongoQuery.addCriteria(new Criteria());
        }
        return mongoQuery;
    }
}
