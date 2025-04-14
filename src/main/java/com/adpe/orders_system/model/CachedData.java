
package com.adpe.orders_system.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CachedData {
    private int code;
    private String message;
    private boolean success;
    private Object data;
}