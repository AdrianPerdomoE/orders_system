package com.adpe.orders_system.model.request_validation.data_sanatize_decorators;

import com.adpe.orders_system.model.CustomRequest;
import com.adpe.orders_system.model.request_validation.DataSanatizeValidator;

public abstract class DataSanatizeDecorator extends DataSanatizeValidator {
    public DataSanatizeValidator dataSanatizeDecoratored;

    public DataSanatizeDecorator(DataSanatizeValidator dataSanatizeValidator) {
        this.dataSanatizeDecoratored = dataSanatizeValidator;
    }

    @Override
     public boolean doValidate(CustomRequest request) {
        // Pre-validation logic can be added here if needed
        preDoValidate(request);// no se controla un falso ya que se espera que las implementaciones de este método lancen excepciones
        return dataSanatizeDecoratored.doValidate(request);
    }

    protected abstract boolean preDoValidate(CustomRequest request);

}

