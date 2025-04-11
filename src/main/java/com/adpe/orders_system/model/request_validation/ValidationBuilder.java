package com.adpe.orders_system.model.request_validation;


public class ValidationBuilder {

    private ValidationHandler validationChain;// El primer validador de la cadena y que representa la cabeza de la cadena
    private ValidationHandler validationTail;// El último validador de la cadena y que representa la cola de la cadena
    // Constructor que inicializa la cadena de validación con el primer validador
  public ValidationBuilder(ValidationHandler validationChain) {
        this.validationChain = validationChain;// El primer validador de la cadena
        this.validationTail = validationChain;// al inicio la cola y la cabeza son el mismo validador
    }
    public ValidationHandler build(){ 
        return this.validationChain; // Devuelve el primer validador de la cadena que representa la cabeza de la cadena
    }

    public ValidationBuilder next(ValidationHandler validationHandler) {
        this.validationTail.setNext(validationHandler);// Establece el siguiente validador en la cola
        this.validationTail = validationHandler; // Actualiza la cola para que apunte al nuevo validador
        // Esto permite que la cadena de validación crezca dinámicamente al agregar nuevos validadores 
        return this;
    }
    // Método estático para crear una nueva instancia de ValidationBuilder con un validador inicial
    public static ValidationBuilder builder(ValidationHandler handler) {
        return new ValidationBuilder(handler);
    }

}
