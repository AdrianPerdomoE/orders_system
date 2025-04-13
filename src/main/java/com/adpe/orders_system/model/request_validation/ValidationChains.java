package com.adpe.orders_system.model.request_validation;

import org.springframework.stereotype.Component;

import com.adpe.orders_system.model.Rols;
import com.adpe.orders_system.util.JwtUtil;

@Component
public class ValidationChains { // Clase que regresa una cadena de validaciones , se usa para regresar cadenas de validaciones estandarizadas para los endpoints de la API, se puede usar para validar el token y el rol del usuario, se puede extender para agregar nuevas validaciones

    private final JwtUtil jwtUtil;

    public ValidationChains(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private ValidationHandler requireAuth() {
        return new AuthValidator(jwtUtil);
    }

    private ValidationHandler requireRole(Rols... roles) {
        return new RolValidator(jwtUtil, roles); // La validacion de rol hereda de Auth por lo tanto ya valida token 
    }

    public ValidationHandler buidStandardWithAdminRolChain() {// Método que regresa una cadena de validaciones estandard para las para las rutas que requiere las validaciones de autenticacion y rol de administrador, se puede extender para agregar nuevas validaciones
        return ValidationBuilder.builder(requireRole(Rols.admin)).build(); 
    } 
    public ValidationHandler buildStandardWithCustomerRolChain() {// Método que regresa una cadena de validaciones estandard para las para las rutas que requiere las validaciones de autenticacion y rol de cliente, se puede extender para agregar nuevas validaciones
        return ValidationBuilder.builder(requireRole(Rols.customer)).build(); 
    }

    public  ValidationHandler buildStandardWithoutRolChain() {// Método que regresa una cadena de validaciones estandard que no requiere validacion de rol, se puede extender para agregar nuevas validaciones
        return ValidationBuilder.builder(requireAuth()).build();
    }

   // public ValidationHandler buildStandardWithoutAuthChain() { // Metodo que regresa una cadena que no requiere validacion de autenticacion, se puede extender para agregar nuevas validaciones
   //     return ValidationBuilder.builder().build(); 
    //}
}
