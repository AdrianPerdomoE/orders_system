package com.adpe.orders_system.config;

import com.adpe.orders_system.model.CustomRequestArgumentResolver;
import com.adpe.orders_system.model.EndpointKey;
import com.adpe.orders_system.model.RequestValidationInterceptor;
import com.adpe.orders_system.model.ValidationChains;
import com.adpe.orders_system.model.request_validation.ValidationHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class WebConfig implements WebMvcConfigurer {


@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("Authorization", "Content-Type");
        }
    };
}
    private final CustomRequestArgumentResolver customRequestArgumentResolver;
    // Almacena el pathMap como un campo para inicializarlo solo una vez
    private final Map<EndpointKey, ValidationHandler> pathMap;
    public WebConfig(CustomRequestArgumentResolver customRequestArgumentResolver, ValidationChains validationChains) {
        this.customRequestArgumentResolver = customRequestArgumentResolver;
          // Inicializa el pathMap una sola vez en el constructor
          this.pathMap = new HashMap<>();
          this.pathMap.put(new EndpointKey("GET", "/api/products/{id}"), validationChains.buildCompleteChainForProductCachedEndpoint());
          this.pathMap.put(new EndpointKey("POST", "/api/products/"), validationChains.buildCompleteChainForProductCreationEndpoint());
          this.pathMap.put(new EndpointKey("/api/**"), validationChains.buildStandardWithAdminRolChain());
          this.pathMap.put(new EndpointKey("POST", "/api/auth/**"), ValidationChains.NO_VALIDATION_HANDLER());// Se deja abierto el endpoint de autenticación para poder mostrar funcionalidad de la app
          this.pathMap.put(new EndpointKey("POST","/api/users/"), ValidationChains.NO_VALIDATION_HANDLER());// Se deja abierto el endpoint de creación de usuarios para poder mostrar funcionalidad de la app
          this.pathMap.put(new EndpointKey("POST", "/api/orders/"), validationChains.buildStandardWithAdminCustomerRolChain());
        this.pathMap.put(new EndpointKey("POST", "/api/products/query/many"), ValidationChains.NO_VALIDATION_HANDLER());
    }

    
    @Bean
    public Map<EndpointKey, ValidationHandler> pathMap() {
        return pathMap;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new RequestValidationInterceptor(pathMap));
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customRequestArgumentResolver);
    }
}
