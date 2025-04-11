package com.adpe.orders_system.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class EnvConfig {

    @PostConstruct
    public void loadEnv() {
        if (System.getenv("DOCKER_ENV") == null) { // Solo carga .env si no está en un contenedor
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> {
                if (System.getenv(entry.getKey()) == null) { // Evita sobrescribir variables del sistema
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });
            System.out.println("✅ Variables cargadas desde .env");
        } else {
            System.out.println("🚀 Ejecutando en Docker, usando variables del sistema");
        }
    }
}
