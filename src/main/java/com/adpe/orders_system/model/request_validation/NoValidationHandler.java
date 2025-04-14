package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.model.CustomRequest;

public class NoValidationHandler extends ValidationHandler {

    @Override
    protected boolean doValidate(CustomRequest request) {
        return true; // No validation, always returns true
    }

}
