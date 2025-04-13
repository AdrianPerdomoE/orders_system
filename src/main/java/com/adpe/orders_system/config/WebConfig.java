package com.adpe.orders_system.config;

import com.adpe.orders_system.model.CustomRequestArgumentResolver;
import com.adpe.orders_system.model.request_validation.RequestValidationInterceptor;
import com.adpe.orders_system.model.request_validation.ValidationChains;
import com.adpe.orders_system.model.request_validation.ValidationHandler;
import com.adpe.orders_system.util.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CustomRequestArgumentResolver customRequestArgumentResolver;
    private final JwtUtil jwtUtil;

    public WebConfig(CustomRequestArgumentResolver customRequestArgumentResolver, JwtUtil jwtUtil) {
        this.customRequestArgumentResolver = customRequestArgumentResolver;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        ValidationChains chains = new ValidationChains(jwtUtil);

        Map<String, Function<HttpServletRequest, ValidationHandler>> pathMap = new HashMap<>();
        pathMap.put("/api/orders", req -> chains.buildStandardWithCustomerRolChain());
        pathMap.put("/api/admin", req -> chains.buidStandardWithAdminRolChain());

        registry.addInterceptor(new RequestValidationInterceptor(pathMap));
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customRequestArgumentResolver);
    }
}