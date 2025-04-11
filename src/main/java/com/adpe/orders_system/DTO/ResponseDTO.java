package com.adpe.orders_system.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    private boolean success; // Indica si la operación fue exitosa
    private int code;        // Código de respuesta (ej. 200, 404, 500, etc.)
    private String message;  // Mensaje descriptivo
    private T data;          // Datos devueltos (puede ser null si no aplica)

    public ResponseDTO(String message, boolean success, int code) {
        this.message = message;
        this.success = success;
        this.code = code;
    }

}