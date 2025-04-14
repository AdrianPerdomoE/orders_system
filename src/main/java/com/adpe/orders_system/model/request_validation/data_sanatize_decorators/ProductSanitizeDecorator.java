package com.adpe.orders_system.model.request_validation.data_sanatize_decorators;

import com.adpe.orders_system.DTO.Product;
import com.adpe.orders_system.error.ValidationChainError;
import com.adpe.orders_system.model.CustomRequest;
import com.adpe.orders_system.model.request_validation.DataSanatizeValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductSanitizeDecorator extends DataSanatizeDecorator {

    public ProductSanitizeDecorator(DataSanatizeValidator dataSanatizeValidator) {
        super(dataSanatizeValidator);
    }

    @Override
    protected boolean preDoValidate(CustomRequest request) {
        String body = request.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        Product product;
        try {
            product = objectMapper.readValue(body, Product.class);
         
        } catch (JsonProcessingException e) {
            throw new ValidationChainError("Invalid JSON format for Product.");
        }


        // Validar que el precio sea mayor a 0
        if (product.getPrice() <= 0) {
            throw new ValidationChainError("Product price must be greater than 0.");
        }

        // Validar que el stock sea mayor a 0
        if (product.getStock() <= 0) {
            throw new ValidationChainError("Product stock must be greater than 0.");
        }
        // Validar que el nombre no esté vacío
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new ValidationChainError("Product name cannot be empty.");
        }
      

        return true; // Validación exitosa
    }
}