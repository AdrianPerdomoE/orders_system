package com.adpe.orders_system.DTO;


import org.springframework.data.annotation.Id;

import com.adpe.orders_system.model.Rols;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignorar campos desconocidos
public class CustomUser {
    @Id
    public String _id;
    public String name;
    
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    
    public Rols rol;

    public String getRol(){
        return this.rol.getString();
    }
    
    public void setRol(String rol) {
        this.rol = Rols.fromString(rol);
    }
}
