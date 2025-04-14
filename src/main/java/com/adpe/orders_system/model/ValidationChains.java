package com.adpe.orders_system.model;

import org.springframework.stereotype.Component;

import com.adpe.orders_system.model.request_validation.AuthValidator;
import com.adpe.orders_system.model.request_validation.BruteForceValidator;
import com.adpe.orders_system.model.request_validation.CacheValidator;
import com.adpe.orders_system.model.request_validation.DataSanatizeValidator;
import com.adpe.orders_system.model.request_validation.NoValidationHandler;
import com.adpe.orders_system.model.request_validation.RolValidator;
import com.adpe.orders_system.model.request_validation.SimpleDataSanatizeValidator;
import com.adpe.orders_system.model.request_validation.ValidationBuilder;
import com.adpe.orders_system.model.request_validation.ValidationHandler;
import com.adpe.orders_system.model.request_validation.data_sanatize_decorators.ProductSanitizeDecorator;
import com.adpe.orders_system.service.CacheService;
import com.adpe.orders_system.util.JwtUtil;

@Component
public class ValidationChains { // Clase que regresa una cadena de validaciones , se usa para regresar cadenas de validaciones estandarizadas para los endpoints de la API, se puede usar para validar el token y el rol del usuario, se puede extender para agregar nuevas validaciones

    private final JwtUtil jwtUtil;
    private final CacheService cacheService; // Se inyecta el servicio de cache para la validacion de fuerza bruta
    public ValidationChains(JwtUtil jwtUtil, CacheService cacheService) { // Constructor que inyecta el servicio de jwt y el servicio de cache
        this.jwtUtil = jwtUtil;
        this.cacheService = cacheService; // Se inyecta el servicio de cache para la validacion de fuerza bruta
    }

    private ValidationHandler requireAuth() {
        return new AuthValidator(jwtUtil);
    }

    private ValidationHandler requireRole(Rols... roles) {
        return new RolValidator(jwtUtil, roles); // La validacion de rol hereda de Auth por lo tanto ya valida token 
    }
    private ValidationHandler requireBruteForceValidation(){
        return new BruteForceValidator(cacheService); 
    }
    private ValidationHandler requireCacheValidation(){
        return new CacheValidator(cacheService); 
    }
    private ValidationHandler requiereDataSanatizeValidation(){
        return new SimpleDataSanatizeValidator();
    }
    private ValidationHandler requireProductDataSanitazion(){
        DataSanatizeValidator simpleSanatize =(DataSanatizeValidator) requiereDataSanatizeValidation(); 
        return (ValidationHandler) new ProductSanitizeDecorator(simpleSanatize); 
    }
    public ValidationHandler buildStandardWithAdminRolChain() {// Método que regresa una cadena de validaciones estandard para las para las rutas que requiere las validaciones de autenticacion y rol de administrador, se puede extender para agregar nuevas validaciones
        return ValidationBuilder.builder(requireBruteForceValidation()).next(requireRole(Rols.admin)).build(); 
    } 
    public ValidationHandler buildStandardWithCustomerRolChain() {// Método que regresa una cadena de validaciones estandard para las para las rutas que requiere las validaciones de autenticacion y rol de cliente, se puede extender para agregar nuevas validaciones
        return ValidationBuilder.builder(requireBruteForceValidation()).next(requireRole(Rols.customer)).build(); 
    }
    public ValidationHandler buildCompleteChainForProductCreationEndpoint() {
        return ValidationBuilder.builder(requireBruteForceValidation()).next(requireRole(Rols.admin)).next(requireProductDataSanitazion()).build(); 
    }
    public ValidationHandler buildCompleteChainForProductCachedEndpoint() {// prueba de cadena de validaciones para el endpoint de productos con cache, sera chacheado el endpoint de obtener 1 producto
        return ValidationBuilder.builder(requireBruteForceValidation()).next(requireRole(Rols.admin,Rols.customer)).next(requireCacheValidation()).build(); 
    }   
    public ValidationHandler buildStandardWithAdminCustomerRolChain() {// Método que regresa una cadena de validaciones estandard para las para las rutas que requiere las validaciones de autenticacion y rol de empleado, se puede extender para agregar nuevas validaciones
        return ValidationBuilder.builder(requireRole(Rols.admin,Rols.customer)).build(); 
    }

    public  ValidationHandler buildStandardWithoutRolChain() {// Método que regresa una cadena de validaciones estandard que no requiere validacion de rol, se puede extender para agregar nuevas validaciones
        return ValidationBuilder.builder(requireAuth()).build();
    }

    public static ValidationHandler NO_VALIDATION_HANDLER() {//Metodo que regresa una cadena sin validaciones;
        return new NoValidationHandler(); 
    }

   // public ValidationHandler buildStandardWithoutAuthChain() { // Metodo que regresa una cadena que no requiere validacion de autenticacion, se puede extender para agregar nuevas validaciones
   //     return ValidationBuilder.builder().build(); 
    //}
}
