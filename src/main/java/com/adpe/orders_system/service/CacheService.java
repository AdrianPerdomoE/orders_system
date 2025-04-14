package com.adpe.orders_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.adpe.orders_system.model.CachedData;
import com.adpe.orders_system.model.CustomRequest;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Guardar un valor en Redis con TTL (tiempo de vida)
    public void saveToCache(String key, Object value, long ttlSeconds) {
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    public void saveCachedData(String key, CachedData value) {
        redisTemplate.opsForValue().set(key, value, 60, TimeUnit.SECONDS);
    }
    // Obtener un valor desde Redis
    public Object getFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Eliminar un valor del caché
    public void removeFromCache(String key) {
        redisTemplate.delete(key);
    }

     public String generateCacheKey(CustomRequest request) {
        // Generar una clave única basada en los datos de la solicitud
        // Por ejemplo, puedes usar el endpoint y los parámetros de la solicitud
        return "CACHE:" + request.getMethod() + request.getPath();
    }

    public String generateCacheKey(String method,String path, String id) {
        // Generar una clave única basada en el endpoint y los parámetros
        return "CACHE:" + method + path + id;
    }
}
