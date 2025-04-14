package com.adpe.orders_system.controller;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.CustomUser;
import com.adpe.orders_system.DTO.LoginDTO;
import com.adpe.orders_system.DTO.LoginResponseDTO;
import com.adpe.orders_system.DTO.ResponseDTO;
import com.adpe.orders_system.model.query.Operator;
import com.adpe.orders_system.model.query.QueryProperty;
import com.adpe.orders_system.service.UserService;
import com.adpe.orders_system.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
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
    @Operation(summary = "Iniciar sesión", description = "Inicia sesión y devuelve un token JWT")
    public ResponseDTO<LoginResponseDTO> login(@RequestBody LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword())
        );
        // Se genera el query para obtener el usuario
        CustomQuery query = new CustomQuery();
        query.getProperties().add(new QueryProperty("name", Operator.EQUALS, request.getName()));
        CustomUser user = userService.getOne(query);
        String token = jwtUtil.generateToken(user);
        return new ResponseDTO<>(true,200,"Login exitoso", new LoginResponseDTO(token));    
    }
}
