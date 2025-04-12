package com.adpe.orders_system.model.request_validation;

import com.adpe.orders_system.model.CustomRequest;



public class BruteForceValidator extends ValidationHandler {

    private static final int MAX_REQUESTS = 5; // Maximum number of requests allowed
    private static final long TIME_FRAME = 60000; // Time frame in milliseconds (1 minute)
    
    @Override
    protected boolean doValidate(CustomRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doValidate'");
    }


}
