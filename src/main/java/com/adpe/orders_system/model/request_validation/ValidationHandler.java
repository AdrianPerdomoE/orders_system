package com.adpe.orders_system.model.request_validation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class ValidationHandler {
    public ValidationHandler next; // Next handler in the chain
    // Método principal para validar la solicitud
    public boolean validate(HttpServletRequest request) {
        if (doValidate(request)) {
            // Si la validación actual pasa, llama al siguiente validador
            if (next != null) {
                return next.validate(request);
            }
            return true; // Si no hay más validadores, la validación pasa
        }
        return false; // Si la validación actual falla, detiene la cadena
    }

    // Método abstracto que las clases hijas deben implementar
    protected abstract boolean doValidate(HttpServletRequest request);

    
}
