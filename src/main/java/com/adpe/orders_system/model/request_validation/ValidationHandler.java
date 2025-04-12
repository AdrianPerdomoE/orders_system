package com.adpe.orders_system.model.request_validation;
import com.adpe.orders_system.error.ValidationChainError;
import com.adpe.orders_system.model.CustomRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class ValidationHandler {
    public ValidationHandler next; // Next handler in the chain
    // Método principal para validar la solicitud
    public boolean validate(CustomRequest request) {
        if (doValidate(request)) {
            // Si la validación actual pasa, llama al siguiente validador
            if (next != null) {
                return next.validate(request);
            }
            return true; // Si no hay más validadores, la validación pasa
        }
        throw new ValidationChainError("Validation failed in " + this.getClass().getSimpleName());
    }

    // Método abstracto que las clases hijas deben implementar
    protected abstract boolean doValidate(CustomRequest request);

    
}
