package com.adpe.orders_system.config;

import com.adpe.orders_system.model.EndpointKey;
import com.adpe.orders_system.model.request_validation.AuthValidator;
import com.adpe.orders_system.model.request_validation.ValidationHandler;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import io.swagger.v3.oas.models.Operation;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import io.swagger.v3.oas.models.Components;
@Configuration
public class SwaggerConfig {

    private final Map<EndpointKey, ValidationHandler> pathMap;

    public SwaggerConfig(Map<EndpointKey, ValidationHandler> pathMap) {
        this.pathMap = pathMap;
    }

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Ordenes")
                        .version("1.0")
                        .description("Documentación de la API del sistema de ordenes con OpenAPI y Swagger"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))); // Define el esquema de seguridad como Bearer Token
    }
    @Bean
    public OperationCustomizer customizeOperation() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (handlerMethod != null) {
                // Obtén la ruta completa y el método HTTP del endpoint
                String path = getFullPathFromHandlerMethod(handlerMethod);
                String httpMethod = getHttpMethodFromHandlerMethod(handlerMethod).toUpperCase();
            
             // Verifica si el endpoint está en el pathMap y requiere autorización
                boolean requiresAuth = pathMap.entrySet().stream()
                                                .sorted(Map.Entry.comparingByKey()) // Ordena por especificidad usando compareTo de EndpointKey
                                                .filter(entry -> entry.getKey().matches(httpMethod, path)) // Encuentra el primer path que coincide
                                                .findFirst() // Toma el primer path que cumple con la especificación
                                                .map(entry -> hasAuthValidator(entry.getValue())) // Verifica si requiere autorización
                                                .orElse(false); // Si no hay coincidencias, no requiere autorización
                if (requiresAuth) {
                    operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
               
                } 
    
                // Verifica si los parámetros no son nulos antes de filtrarlos
                if (operation.getParameters() != null) {
                    operation.setParameters(
                            operation.getParameters().stream()
                                    .filter(parameter -> !"customRequest".equals(parameter.getName()))
                                    .toList()
                    );
                }
            }
            return operation;
        };
    }
    
    /**
     * Construye la ruta completa del endpoint combinando el RequestMapping del controlador y del método.
     *
     * @param handlerMethod El HandlerMethod del endpoint.
     * @return La ruta completa del endpoint.
     */
    private String getFullPathFromHandlerMethod(HandlerMethod handlerMethod) {
        // Obtén el RequestMapping del controlador
        RequestMapping controllerMapping = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
        String controllerPath = (controllerMapping != null && controllerMapping.value().length > 0)
                ? controllerMapping.value()[0]
                : "";
    
        // Obtén el RequestMapping del método
        String methodPath = getPathFromHandlerMethod(handlerMethod);
    
        // Combina ambos paths
        return controllerPath + methodPath;
    }
    
    /**
     * Obtiene la ruta del endpoint desde el HandlerMethod.
     *
     * @param handlerMethod El HandlerMethod del endpoint.
     * @return La ruta del endpoint.
     */
    @SuppressWarnings("null")
    private String getPathFromHandlerMethod(HandlerMethod handlerMethod) {
        if (handlerMethod.getMethodAnnotation(RequestMapping.class) != null) {
            return handlerMethod.getMethodAnnotation(RequestMapping.class).value()[0];
        } else if (handlerMethod.getMethodAnnotation(GetMapping.class) != null) {
            return handlerMethod.getMethodAnnotation(GetMapping.class).value()[0];
        } else if (handlerMethod.getMethodAnnotation(PostMapping.class) != null) {
            return handlerMethod.getMethodAnnotation(PostMapping.class).value()[0];
        } else if (handlerMethod.getMethodAnnotation(PutMapping.class) != null) {
            return handlerMethod.getMethodAnnotation(PutMapping.class).value()[0];
        } else if (handlerMethod.getMethodAnnotation(DeleteMapping.class) != null) {
            return handlerMethod.getMethodAnnotation(DeleteMapping.class).value()[0];
        }
        return ""; // Devuelve una cadena vacía si no se encuentra ninguna anotación
    }
    
    /**
     * Obtiene el método HTTP del endpoint desde el HandlerMethod.
     *
     * @param handlerMethod El HandlerMethod del endpoint.
     * @return El método HTTP (GET, POST, etc.).
     */
    @SuppressWarnings("null")
    private String getHttpMethodFromHandlerMethod(HandlerMethod handlerMethod) {
        if (handlerMethod.getMethodAnnotation(GetMapping.class) != null) {
            return "GET";
        } else if (handlerMethod.getMethodAnnotation(PostMapping.class) != null) {
            return "POST";
        } else if (handlerMethod.getMethodAnnotation(PutMapping.class) != null) {
            return "PUT";
        } else if (handlerMethod.getMethodAnnotation(DeleteMapping.class) != null) {
            return "DELETE";
        } else if (handlerMethod.getMethodAnnotation(RequestMapping.class) != null) {
            RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
            if (requestMapping.method().length > 0) {
                return requestMapping.method()[0].name();
            }
        }
        return ""; // Devuelve una cadena vacía si no se encuentra ningún método HTTP
    }
    /**
     * Función recursiva para verificar si un ValidationHandler o alguno de sus encadenados
     * es una instancia o herencia de AuthValidator.
     *
     * @param handler El ValidationHandler a verificar.
     * @return true si se encuentra un AuthValidator en la cadena, false en caso contrario.
     */
    private boolean hasAuthValidator(ValidationHandler handler) {
        if (handler == null) {
            return false;
        }
        if (handler instanceof AuthValidator) {
            return true;
        }
        return hasAuthValidator(handler.getNext()); // Llama recursivamente al siguiente en la cadena
    }
}