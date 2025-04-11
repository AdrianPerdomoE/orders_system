package com.adpe.orders_system.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Guardar un valor en Redis con TTL (tiempo de vida)
    public void saveToCache(String key, Object value, long ttlSeconds) {
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    // Obtener un valor desde Redis
    public Object getFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Eliminar un valor del caché
    public void removeFromCache(String key) {
        redisTemplate.delete(key);
    }
}
