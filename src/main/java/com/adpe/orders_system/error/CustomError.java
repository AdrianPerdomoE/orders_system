package com.adpe.orders_system.error;


public abstract class  CustomError extends RuntimeException {
    public int code;

    public CustomError(String message, int code) {
        super(message);
        this.code = code;
    }
    public CustomError(String message) {
        super(message);
        this.code = 400;
    }
}
