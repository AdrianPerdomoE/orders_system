package com.adpe.orders_system.error;

public class ValidationChainError  extends CustomError{
    public ValidationChainError(String message) {
        super(message, 400);  // 400 Bad Request
    }
}
