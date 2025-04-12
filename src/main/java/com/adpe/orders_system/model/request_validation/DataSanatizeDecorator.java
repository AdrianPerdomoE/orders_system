package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.model.CustomRequest;

public abstract class DataSanatizeDecorator extends DataSanatizeValidator {
    public DataSanatizeValidator dataSanatizeDecoratored;

    public DataSanatizeDecorator(DataSanatizeValidator dataSanatizeValidator) {
        this.dataSanatizeDecoratored = dataSanatizeValidator;
    }

    @Override
     protected boolean doValidate(CustomRequest request) {
        return dataSanatizeDecoratored.doValidate(request);
    }

}

