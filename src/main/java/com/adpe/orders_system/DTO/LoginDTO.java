package com.adpe.orders_system.DTO;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Data
public class LoginDTO {
    @NonNull
    private String name;
    @NonNull
    private String password;

}
