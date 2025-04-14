package com.adpe.orders_system.model;

import com.adpe.orders_system.DTO.CustomUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class CustomRequest {

    private final HttpServletRequest rawRequest;
    private CustomUser authenticatedUser; // ahora puede cambiarse
    private final String clientIp;
    private Object data; // se puede usar para almacenar cualquier objeto adicional
    private final Map<String, String> headers;
    private final Map<String, String[]> parameters;
    private String body; // se carga bajo demanda
    private Map<String, String> pathVariables = new HashMap<>();
    private CustomUser user; // se puede usar para almacenar el usuario autenticado
    public CustomRequest(HttpServletRequest request) {
        this.rawRequest = request;
        this.clientIp = extractClientIp(request);
        this.headers = extractHeaders(request);

        this.parameters = request.getParameterMap();
        
    }

    public CustomRequest(HttpServletRequest request, CustomUser user) {
        this(request);
        this.authenticatedUser = user;
    }

    // Extraer IP del cliente
    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip != null && !ip.isEmpty()) ? ip : request.getRemoteAddr();
    }

    // Extraer headers como Map<String, String>
    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headerMap.put(header.toLowerCase(), request.getHeader(header)); // Normaliza a minúsculas
        }
        return headerMap;
    }

    // ================= Utilidades =================

    public String getHeader(String name) {
        name = name.toLowerCase(); // Normaliza a minúsculas
        return headers.getOrDefault(name, null);
    }

    public boolean hasHeader(String name) {
        name = name.toLowerCase(); // Normaliza a minúsculas
        return headers.containsKey(name);
    }

    public boolean isAuthenticated() {
        return authenticatedUser != null;
    }

    public boolean hasRole(String... allowedRoles) {
        if (authenticatedUser == null) return false;
        String userRole = authenticatedUser.getRol();
        for (String role : allowedRoles) {
            if (userRole.equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    public String getMethod() {
        return rawRequest.getMethod();
    }

    public String getPath() {
        return rawRequest.getRequestURI();
    }

    public String getQueryParam(String key) {
        String[] values = parameters.get(key);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    public Map<String, String> getFlatQueryParams() {
        return parameters.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue()[0]
                ));
    }

    // ================= Nuevos métodos =================

    // Leer el cuerpo como String (una sola vez)
  public String getBody() {
    if (body == null) {
        try {
            if (rawRequest instanceof ContentCachingRequestWrapper wrapper) {
                byte[] content = wrapper.getContentAsByteArray();
                body = new String(content, StandardCharsets.UTF_8);
            } else {
                body = StreamUtils.copyToString(rawRequest.getInputStream(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            body = null;
        }
    }
    return body;
}


    // Extraer el token JWT
    public String getJwtToken() {
        String authHeader = getHeader("authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Manejo de path variables (opcional, se puede llenar desde un filtro o handler)
    public void setPathVariable(String key, String value) {
        pathVariables.put(key, value);
    }

    public String getPathVariable(String key) {
        return pathVariables.get(key);
    }

    public boolean hasPathVariable(String key) {
        return pathVariables.containsKey(key);
    }
}
