package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.error.ValidationChainError;
import com.adpe.orders_system.model.CustomRequest;

public class SimpleDataSanatizeValidator extends DataSanatizeValidator{

    @Override
    public boolean doValidate(CustomRequest request) {

        if (request.getBody() == null) {
            throw new ValidationChainError("Data is null.");
        }
        return true;
    }


}
