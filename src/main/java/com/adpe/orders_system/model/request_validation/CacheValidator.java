package com.adpe.orders_system.model.request_validation;
import com.adpe.orders_system.error.CachedDataException;
import com.adpe.orders_system.model.CachedData;
import com.adpe.orders_system.model.CustomRequest;
import com.adpe.orders_system.service.CacheService;

public class CacheValidator extends ValidationHandler {

    private final CacheService cacheService;

    public CacheValidator(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    protected boolean doValidate(CustomRequest request) {
        String cacheKey =  cacheService.generateCacheKey(request); // Generar una clave única para la solicitud
  
        // Verificar si existe una respuesta cacheada
        CachedData cachedResponse = (CachedData) cacheService.getFromCache(cacheKey);
       
        if (cachedResponse != null) {
            // Si hay una respuesta cacheada, devolverla directamente
            handleCachedResponse(cachedResponse);
        }

        return true; // Si no hay respuesta cacheada, continuar con la validación
    }

   
    private boolean handleCachedResponse(CachedData cachedResponse) {
        
        // print so we can know that we are returning a cached response
        System.out.println("Returning cached response: " + cachedResponse);
        throw new CachedDataException(cachedResponse); // Lanzar la excepción con la respuesta cacheada
    }
}