package com.adpe.orders_system.model;

import org.springframework.web.bind.support.WebDataBinderFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
@Component
public class CustomRequestArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return CustomRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
        @NonNull MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer,
        @Nullable  NativeWebRequest webRequest,
        @Nullable  WebDataBinderFactory binderFactory
    ) {
        if(webRequest == null) {
           throw new IllegalArgumentException("webRequest cannot be null");
        }
        
        HttpServletRequest rawRequest = ((ServletWebRequest) webRequest).getRequest();
        CustomRequest custom = (CustomRequest) rawRequest.getAttribute("customRequest");
        if (custom != null) return custom;
        return new CustomRequest(rawRequest);
    }

}
