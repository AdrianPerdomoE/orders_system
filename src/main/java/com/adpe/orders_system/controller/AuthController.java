package com.adpe.orders_system.controller;

import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.CustomUser;
import com.adpe.orders_system.model.query.Operator;
import com.adpe.orders_system.model.query.QueryProperty;
import com.adpe.orders_system.service.UserService;
import com.adpe.orders_system.util.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Controlador de Autenticación")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("name"), request.get("password"))
        );
        // Se genera el query para obtener el usuario
        CustomQuery query = new CustomQuery();
        query.getProperties().add(new QueryProperty("name", Operator.EQUALS, request.get("name")));
        CustomUser user = userService.getOne(query);
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
