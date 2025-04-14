package com.adpe.orders_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.lang.NonNull;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Value("${MONGO_URI}")
    private String mongoUri; ;
    @Override
    public boolean autoIndexCreation() {
        return true;
    }

    @Override
    @NonNull
    protected String getDatabaseName() {
        return "orders_system";
    }

    @Bean
    @Override
    @NonNull
    public MongoClient mongoClient() {
        // Configura el cliente de MongoDB con el string de conexión
        return MongoClients.create(mongoUri);
    }
}