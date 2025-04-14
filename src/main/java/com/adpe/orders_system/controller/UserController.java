package com.adpe.orders_system.controller;

import com.adpe.orders_system.DTO.CustomUser;
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
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios")
@CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:3000") // Uncomment this line to restrict CORS to a specific origin
public class UserController extends AbstractController<CustomUser> {

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
    public ResponseDTO<CustomUser> create(@Valid @RequestBody CustomUser requestBody) {
        if (requestBody.get_id() != null) {
            throw new  UnExpectedArgumentError("ID should not be provided when creating a new user.");
        }

        CustomUser newUser  = getService().create(requestBody);

        if (newUser == null) {
            return new ResponseDTO<CustomUser>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create user", null);
        }

        return new ResponseDTO<CustomUser>(true,HttpStatus.CREATED.value(), "User created successfully", newUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener Usuario", description = "Obtener un usuario por su ID") 
    @Override
    public ResponseDTO<CustomUser> getById(@PathVariable String id) {
        CustomUser user = getService().getById(id);
        return new ResponseDTO<CustomUser>(true,HttpStatus.OK.value(), "User found", user);
    }

    @PostMapping("/query/one")
    @Operation(summary = "Obtener Usuario", description = "Obtener un usuario por un query")
    @Override
    public ResponseDTO<CustomUser> getOne(@Valid @RequestBody CustomQuery requestBody) {
        requestBody.setLimit(1); // Set limit to 1 for single user retrieval
        CustomUser user = getService().getOne(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "User found", user);
    }

    @PostMapping("/query/many")
    @Operation(summary = "Obtener Usuarios", description = "Obtener usuarios por un query")
    @Override
    public ResponseDTO<List<CustomUser>> getMany(@Valid @RequestBody CustomQuery requestBody) {
        List<CustomUser> users = getService().getMany(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Users found", users);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Usuario", description = "Actualizar un usuario por su ID")
    @Override
    public ResponseDTO<CustomUser> updateOne(@PathVariable String id, @Valid @RequestBody CustomUser requestBody) {
        if (requestBody.get_id() != null) {
            throw new UnExpectedArgumentError("ID should not be provided when updating a user.");
        }
        if (id == null) {
            throw new UnExpectedArgumentError("ID should be provided when updating a user.");
        }
        CustomUser updatedUser = getService().updateOne(id, requestBody);
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