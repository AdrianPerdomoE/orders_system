package com.adpe.orders_system.error;

public class UnExpectedArgumentError  extends CustomError{

   public UnExpectedArgumentError(String message) {
        super(message, 400);
    }

    
}
