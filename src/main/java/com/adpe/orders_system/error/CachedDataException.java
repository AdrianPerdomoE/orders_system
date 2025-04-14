package com.adpe.orders_system.error;

import com.adpe.orders_system.model.CachedData;



public class CachedDataException extends RuntimeException {
    //  Esta es una excepción personalizada que se lanza cuando se quiere devolver una respuesta cacheada
    //  en lugar de procesar la solicitud normalmente. Se utiliza para manejar la lógica de caché en el sistema.
    private CachedData CachedData;
    
    public CachedDataException(CachedData data) {
        super(data.getMessage());
        CachedData = data;
    }

    public CachedData getCachedData() {
        return CachedData;
    }
}
