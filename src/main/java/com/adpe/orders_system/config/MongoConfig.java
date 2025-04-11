package com.adpe.orders_system.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;


@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    
    @SuppressWarnings("null")
    @Override
    protected String getDatabaseName() {
        return "orders_system";
    }
    
    @Override
    public boolean autoIndexCreation() {
        return true;
    }
}
