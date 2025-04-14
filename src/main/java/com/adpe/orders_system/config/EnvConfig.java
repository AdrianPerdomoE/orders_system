package com.adpe.orders_system.config;
import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

  
    public void loadEnv() {
        if (System.getenv("DOCKER_ENV") == null) { // Solo carga .env si no está en un contenedor
            Dotenv dotenv = Dotenv.configure().load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
          
            });
           
            System.out.println("Variables cargadas desde .env");
        } else {
            System.out.println("Ejecutando en Docker, usando variables del sistema");
        }
    }
}
