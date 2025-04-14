package com.adpe.orders_system.controller;

import com.adpe.orders_system.DTO.Order;
import com.adpe.orders_system.error.UnExpectedArgumentError;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.ResponseDTO;
import com.adpe.orders_system.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Órdenes", description = "Endpoints para gestionar órdenes")
@CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:3000") // Uncomment this line to restrict CORS to a specific origin
public class OrderController extends AbstractController<Order> {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected OrderService getService() {
        return orderService;
    }

    @PostMapping("/")
    @Operation(summary = "Crear orden", description = "Crear una nueva orden en el sistema de órdenes")
    @Override
    public ResponseDTO<Order> create(@Valid @RequestBody Order requestBody) {
        if (requestBody.get_id() != null) {
            throw new UnExpectedArgumentError("ID should not be provided when creating a new order.");
        }

        Order newOrder = getService().create(requestBody);

        if (newOrder == null) {
            return new ResponseDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create order", null);
        }

        return new ResponseDTO<>(true, HttpStatus.CREATED.value(), "Order created successfully", newOrder);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden", description = "Obtener una orden por su ID")
    @Override
    public ResponseDTO<Order> getById(@PathVariable String id) {
        Order order = getService().getById(id);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Order found", order);
    }

    @PostMapping("/query/one")
    @Operation(summary = "Obtener orden", description = "Obtener una orden por un query")
    @Override
    public ResponseDTO<Order> getOne(@Valid @RequestBody CustomQuery requestBody) {
        requestBody.setLimit(1); // Set limit to 1 for single retrieval
        Order order = getService().getOne(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Order found", order);
    }

    @PostMapping("/query/many")
    @Operation(summary = "Obtener órdenes", description = "Obtener órdenes por un query")
    @Override
    public ResponseDTO<List<Order>> getMany(@Valid @RequestBody CustomQuery requestBody) {
        List<Order> orders = getService().getMany(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Orders found", orders);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar orden", description = "Actualizar una orden por su ID")
    @Override
    public ResponseDTO<Order> updateOne(@PathVariable String id, @Valid @RequestBody Order requestBody) {
        if (requestBody.get_id() != null) {
            throw new UnExpectedArgumentError("ID should not be provided when updating an order.");
        }
        if (id == null) {
            throw new UnExpectedArgumentError("ID should be provided when updating an order.");
        }
        Order updatedOrder = getService().updateOne(id, requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Order updated successfully", updatedOrder);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden", description = "Eliminar una orden por su ID")
    @Override
    public ResponseDTO<Void> deleteOne(@PathVariable String id) {
        getService().deleteOne(id);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Order deleted successfully", null);
    }
}