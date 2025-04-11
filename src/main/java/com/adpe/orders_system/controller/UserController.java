package com.adpe.orders_system.controller;

import com.adpe.orders_system.DTO.User;
import com.adpe.orders_system.error.UnExpectedArgumentError;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.ResponseDTO;
import com.adpe.orders_system.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios")
@CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:3000") // Uncomment this line to restrict CORS to a specific origin
public class UserController extends AbstractController<User> {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected UserService getService() {
        return userService;
    }

    @PostMapping("/")
    @Operation(summary = "Crear usuarios", description = "Crear un nuevo usuario en el sistema de ordenes") 
    @Override
    public ResponseDTO<User> create(@Valid @RequestBody User requestBody) {
        if (requestBody.get_id() != null) {
            throw new  UnExpectedArgumentError("ID should not be provided when creating a new user.");
        }

        User newUser  = getService().create(requestBody);

        if (newUser == null) {
            return new ResponseDTO<User>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create user", null);
        }

        return new ResponseDTO<User>(true,HttpStatus.CREATED.value(), "User created successfully", newUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener Usuario", description = "Obtener un usuario por su ID") 
    @Override
    public ResponseDTO<User> getById(@PathVariable String id) {
        User user = getService().getById(id);
        return new ResponseDTO<User>(true,HttpStatus.OK.value(), "User found", user);
    }

    @PostMapping("/query/one")
    @Operation(summary = "Obtener Usuario", description = "Obtener un usuario por un query")
    @Override
    public ResponseDTO<User> getOne(@Valid @RequestBody CustomQuery requestBody) {
        requestBody.setLimit(1); // Set limit to 1 for single user retrieval
        User user = getService().getOne(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "User found", user);
    }

    @PostMapping("/query/many")
    @Operation(summary = "Obtener Usuarios", description = "Obtener usuarios por un query")
    @Override
    public ResponseDTO<List<User>> getMany(@Valid @RequestBody CustomQuery requestBody) {
        List<User> users = getService().getMany(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Users found", users);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Usuario", description = "Actualizar un usuario por su ID")
    @Override
    public ResponseDTO<User> updateOne(@PathVariable String id, @Valid @RequestBody User requestBody) {
        if (requestBody.get_id() != null) {
            throw new UnExpectedArgumentError("ID should not be provided when updating a user.");
        }
        if (id == null) {
            throw new UnExpectedArgumentError("ID should be provided when updating a user.");
        }
        User updatedUser = getService().updateOne(id, requestBody);
        return new ResponseDTO<>(true,HttpStatus.OK.value(),  "User updated successfully",updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Usuario", description = "Eliminar un usuario por su ID")
    @Override
    public ResponseDTO<Void> deleteOne(@PathVariable String id) {
        getService().deleteOne(id);
        return new ResponseDTO<>(true,HttpStatus.OK.value(), "User deleted successfully", null);
    }
}