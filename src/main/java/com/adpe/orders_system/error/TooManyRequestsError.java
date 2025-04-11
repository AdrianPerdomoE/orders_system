package com.adpe.orders_system.error;

public class TooManyRequestsError extends CustomError { public TooManyRequestsError(String msg) { super(msg, 429); } }