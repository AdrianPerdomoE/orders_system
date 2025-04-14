package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.model.CustomRequest;

public abstract class DataSanatizeValidator extends ValidationHandler {
    //Clase base para la sanitización de datos
    @Override
    public abstract boolean doValidate(CustomRequest request);
}
