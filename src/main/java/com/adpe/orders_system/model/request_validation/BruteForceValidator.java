package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.error.ValidationChainError;
import com.adpe.orders_system.model.CustomRequest;
import com.adpe.orders_system.service.CacheService;

public class BruteForceValidator extends ValidationHandler {

    private static final int MAX_ATTEMPTS = 50; // Número máximo de intentos permitidos
    private static final long BLOCK_TIME_SECONDS = 300; // Tiempo de bloqueo en segundos (5 minutos)
    private final CacheService cacheService;

    public BruteForceValidator(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    protected boolean doValidate(CustomRequest request) {
        String clientIdentifier = request.getClientIp(); // Usar IP del cliente como identificador

        // Verificar si el cliente está bloqueado
        String blockKey = "BLOCKED:" + clientIdentifier;
        if (cacheService.getFromCache(blockKey) != null) {
            throw new ValidationChainError("Too many attempts. Try again later.");
        }

        // Incrementar el contador de intentos fallidos
        String attemptsKey = "ATTEMPTS:" + clientIdentifier;
        Integer attempts = (Integer) cacheService.getFromCache(attemptsKey);
        if (attempts == null) {
            attempts = 0;
        }

        attempts++;
        cacheService.saveToCache(attemptsKey, attempts, BLOCK_TIME_SECONDS); // Actualizar el contador con TTL

        // Si se excede el número máximo de intentos, bloquear al cliente
        if (attempts >= MAX_ATTEMPTS) {
            cacheService.saveToCache(blockKey, true, BLOCK_TIME_SECONDS); // Bloquear al cliente
            throw new ValidationChainError("Too many failed attempts. You are temporarily blocked.");
        }

        return true; // Validación exitosa
    }

    
}