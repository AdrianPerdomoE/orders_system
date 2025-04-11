package com.adpe.orders_system.error;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import com.adpe.orders_system.DTO.ResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseDTO<Null>> handleResponseStatusException(ResponseStatusException ex) {
        ResponseDTO<Null> response =new ResponseDTO<Null>(ex.getMessage(), false, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    // Manejo de excepciones genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Null>> handleGenericException(Exception ex) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (ex instanceof CustomError) {
            status = ((CustomError) ex).code;
        }
        ResponseDTO<Null> response = new ResponseDTO<Null>(ex.getMessage(), false, status);
        return ResponseEntity.status(status).body(response);
    }
}