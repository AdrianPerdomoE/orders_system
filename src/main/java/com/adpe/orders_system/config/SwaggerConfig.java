package com.adpe.orders_system.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Ordenes")
                        .version("1.0")
                        .description("Documentación de la API del sistema de ordenes con OpenAPI y Swagger"));
    }
}