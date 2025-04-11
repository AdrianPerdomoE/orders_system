package com.adpe.orders_system.error;


public abstract class  CustomError extends RuntimeException {
    public int code;

    CustomError(String message, int code) {
        super(message);
        this.code = code;
    }
    CustomError(String message) {
        super(message);
        this.code = 400;
    }
}
