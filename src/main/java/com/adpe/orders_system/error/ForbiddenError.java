package com.adpe.orders_system.error;

public class ForbiddenError extends CustomError { public ForbiddenError(String msg) { super(msg, 403); } }