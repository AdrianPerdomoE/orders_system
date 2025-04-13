package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.model.CustomRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.function.Function;

public class RequestValidationInterceptor implements HandlerInterceptor {

    private final Map<String, Function<HttpServletRequest, ValidationHandler>> pathValidationMap;

    public RequestValidationInterceptor(Map<String, Function<HttpServletRequest, ValidationHandler>> pathValidationMap) {
        this.pathValidationMap = pathValidationMap;
    }

    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String path = request.getRequestURI();

        if (pathValidationMap.containsKey(path)) {
            ValidationHandler validator = pathValidationMap.get(path).apply(request);
            CustomRequest customRequest = new CustomRequest(request, null);
            validator.validate(customRequest); // Lanza excepción si falla
            request.setAttribute("customRequest", customRequest);
        }
       
        return true;
    }
}
