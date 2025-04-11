package com.adpe.orders_system.error;

public class UnauthorizedError extends CustomError {
    public UnauthorizedError(String message) {
        super(message, 401 );
    }
}
