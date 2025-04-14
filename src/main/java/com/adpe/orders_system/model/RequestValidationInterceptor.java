package com.adpe.orders_system.model;

import com.adpe.orders_system.model.request_validation.ValidationHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import com.mongodb.lang.NonNull;

import java.util.Map;

public class RequestValidationInterceptor implements HandlerInterceptor {

    private final Map<EndpointKey, ValidationHandler> pathValidationMap;
    private final ValidationHandler defaultValidationHandler;
  

    public RequestValidationInterceptor(Map<EndpointKey, ValidationHandler> pathValidationMap, ValidationHandler defaultValidationHandler) {
        this.pathValidationMap = pathValidationMap;
        this.defaultValidationHandler = defaultValidationHandler;
    }
    public RequestValidationInterceptor(Map<EndpointKey, ValidationHandler> pathValidationMap) {
        this.pathValidationMap = pathValidationMap;
        this.defaultValidationHandler = ValidationChains.NO_VALIDATION_HANDLER(); // Asigna cadena vacia si no se proporciona un validador por defecto
    }
    


    @SuppressWarnings("null")
    @Override
    @NonNull
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestMethod = request.getMethod();
        String requestPath = request.getRequestURI();
    
        // Busca un validador para el path actual
        ValidationHandler validationHandler = findValidationHandler(requestMethod, requestPath);
    
        // Crea el CustomRequest
        CustomRequest customRequest = new CustomRequest(request);
    
        // Valida la solicitud
        validationHandler.validate(customRequest);
    
        // Adjunta el CustomRequest al request
        request.setAttribute("customRequest", customRequest);
    
        return true;
    }
    private ValidationHandler findValidationHandler(String requestMethod, String requestPath) {
        return pathValidationMap.entrySet().stream()
                .sorted((Map.Entry.comparingByKey()))
                .filter(entry -> entry.getKey().matches(requestMethod, requestPath))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(defaultValidationHandler);
    }
    
   
}