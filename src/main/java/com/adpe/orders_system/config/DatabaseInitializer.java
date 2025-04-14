package com.adpe.orders_system.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final MongoDatabaseFactory mongoDatabaseFactory;
    private final Seeder seeder;
    public DatabaseInitializer(MongoTemplate mongoTemplate, MongoDatabaseFactory mongoDatabaseFactory, Seeder seeder) {
        this.mongoTemplate = mongoTemplate;
        this.mongoDatabaseFactory = mongoDatabaseFactory;
        this.seeder = seeder;
    }

    @Override
    public void run(String... args) throws Exception {
        String databaseName = "orders_system";
        

        // Obtén el cliente MongoClient desde MongoDatabaseFactory
        try {
            mongoDatabaseFactory.getMongoDatabase(databaseName);
        } catch (Exception e) {
            System.out.println("Error al obtener la base de datos: " + e.getMessage());
            return;
        }
        seeder.seedProducts();

        System.out.println("Database and collection are ready.");
    }
}