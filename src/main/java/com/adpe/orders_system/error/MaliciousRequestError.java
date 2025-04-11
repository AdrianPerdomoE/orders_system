package com.adpe.orders_system.error;

public class MaliciousRequestError extends CustomError { public MaliciousRequestError(String msg) { super(msg, 400); } }