package com.adpe.orders_system.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.adpe.orders_system.model.query.CustomQueryFactory;
import com.adpe.orders_system.model.query.MongoQueryStrategy;

@Configuration
public class QueryBuilderConfig {

    @Bean
    public CustomQueryFactory customQueryFactory() {
        return new CustomQueryFactory(new MongoQueryStrategy());
    }
}