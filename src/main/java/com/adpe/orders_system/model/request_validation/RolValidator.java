package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.model.Rols;

import jakarta.servlet.http.HttpServletRequest;

public abstract class RolValidator extends ValidationHandler {
    private Rols[] allowedRoles; // Array de roles permitidos, se puede usar para validar el rol del usuario, los roles se deben definir en el enum Rols y se debe definir el array de roles permitidos en el constructor de la clase hija

    public Rols[] getAllowedRoles() {
        return allowedRoles;
    }

    @Override
    protected boolean doValidate(HttpServletRequest request) {
        // Obtener el rol del usuario desde la solicitud (puede ser un encabezado, un parámetro de consulta, etc.)        
        return false;
    }

    
}
