package com.adpe.orders_system.DTO;
import java.util.ArrayList;
import java.util.List;

import com.adpe.orders_system.model.query.QueryProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.lang.Nullable;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CustomQuery {

        public CustomQuery() {
                properties = new ArrayList<>();
        }
        
        
        public List<QueryProperty> properties; // Array of query properties
        @Nullable
        public String orderBy; // Field to order by, by default is not set
        @Nullable
        public String orderDirection; // Direction of ordering (asc/desc) field to order by, by default is asc
        @Nullable
        public int limit; // Limit for the number of results, by default is not set
        @Nullable
        public int offset; // Offset for pagination of results, by default is not set

} 




