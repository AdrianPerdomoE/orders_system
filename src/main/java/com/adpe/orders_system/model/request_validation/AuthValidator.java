package com.adpe.orders_system.model.request_validation;


import com.adpe.orders_system.DTO.CustomUser;
import com.adpe.orders_system.util.JwtUtil;
import com.adpe.orders_system.model.CustomRequest;
import com.adpe.orders_system.error.UnauthorizedError;



public class AuthValidator extends ValidationHandler {

    private final JwtUtil jwtUtil;

    public AuthValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean doValidate(CustomRequest request) {

        String token = request.getJwtToken();
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedError("Missing or invalid Authorization header");
        }
        CustomUser user = jwtUtil.extractPayload(token);

        if (user == null || jwtUtil.isTokenExpired(token)) {
            throw new UnauthorizedError("Invalid or expired token");
        }

        request.setUser(user); // Aquí adjuntamos el usuario al request
        return true;
    }

}
