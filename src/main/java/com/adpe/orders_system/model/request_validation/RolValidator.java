package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.error.UnauthorizedError;
import com.adpe.orders_system.model.CustomRequest;
import com.adpe.orders_system.model.Rols;
import com.adpe.orders_system.util.JwtUtil;

public class RolValidator extends AuthValidator {
    
    public RolValidator(JwtUtil jwtUtil, Rols... allowedRoles) {
        super(jwtUtil);
        this.allowedRoles = allowedRoles;
    }

    private Rols[] allowedRoles; // Array de roles permitidos, se puede usar para validar el rol del usuario, los roles se deben definir en el enum Rols y se debe definir el array de roles permitidos en el constructor de la clase hija

    public Rols[] getAllowedRoles() {
        return allowedRoles;
    }

    @Override
    protected boolean doValidate(CustomRequest request) {
        
        if (super.doValidate(request)) { // Primero validamos el token
            String userRole = request.getUser().getRol(); // Obtenemos el rol del usuario desde el token
            for (Rols role : allowedRoles) { // Comparamos el rol del usuario con los roles permitidos
                if (userRole == role.getString()) { // Si el rol del usuario es uno de los roles permitidos, se permite el acceso
                    return true; // Si el rol del usuario es uno de los roles permitidos, se permite el acceso
                }
            }
        }
         throw new UnauthorizedError("User does not have the required role"); // Si el rol del usuario no es uno de los roles permitidos, se lanza una excepción de error 401   
    }

    
}
