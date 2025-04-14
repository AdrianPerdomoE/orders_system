package com.adpe.orders_system.model;

import com.adpe.orders_system.model.request_validation.ValidationHandler;
import com.mongodb.lang.NonNull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.function.Function;

public class RequestValidationInterceptor implements HandlerInterceptor {

    private final Map<EndpointKey, Function<HttpServletRequest, ValidationHandler>> pathValidationMap;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public RequestValidationInterceptor(Map<EndpointKey, Function<HttpServletRequest, ValidationHandler>> pathValidationMap) {
        this.pathValidationMap = pathValidationMap;
    }

    @Override
    @NonNull
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) {

        String requestMethod = request.getMethod();
        String requestPath = request.getRequestURI();

        for (Map.Entry<EndpointKey, Function<HttpServletRequest, ValidationHandler>> entry : pathValidationMap.entrySet()) {
            EndpointKey key = entry.getKey();

            if (key.getMethod().equalsIgnoreCase(requestMethod) && pathMatcher.match(key.getPath(), requestPath)) {
                // Coincidencia encontrada
                Map<String, String> pathVariables = pathMatcher.extractUriTemplateVariables(key.getPath(), requestPath);
                CustomRequest customRequest = new CustomRequest(request, null);

                // Inyectamos las variables de ruta encontradas
                for (Map.Entry<String, String> variable : pathVariables.entrySet()) {
                    customRequest.setPathVariable(variable.getKey(), variable.getValue());
                }

                ValidationHandler validator = entry.getValue().apply(request);
                validator.validate(customRequest);
                request.setAttribute("customRequest", customRequest);
                break;
            }
        }

        return true;
    }
}
