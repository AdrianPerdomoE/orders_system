package com.adpe.orders_system.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class CachedBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Verificamos si es una petición HTTP válida
        if (request instanceof HttpServletRequest httpServletRequest) {
            // Envolvemos la petición para que su InputStream se pueda leer múltiples veces
            ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(httpServletRequest);

            // Continuamos el filtro con la nueva request cacheada
            chain.doFilter(cachedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
