package com.adpe.orders_system.error;


public class NotFoundError extends CustomError{

    public NotFoundError(String message) {
        super(message,404);
    }
}

