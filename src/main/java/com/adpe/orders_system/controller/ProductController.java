package com.adpe.orders_system.controller;

import com.adpe.orders_system.DTO.Product;
import com.adpe.orders_system.error.UnExpectedArgumentError;
import com.adpe.orders_system.model.CachedData;
import com.adpe.orders_system.model.CustomRequest;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.ResponseDTO;
import com.adpe.orders_system.service.CacheService;
import com.adpe.orders_system.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Endpoints para gestionar productos")
@CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:3000") // Uncomment this line to restrict CORS to a specific origin
public class ProductController extends AbstractController<Product> {

    private final ProductService productService;
    private final CacheService cacheService;
    public ProductController(ProductService productService, CacheService cacheService) {
        this.productService = productService;
        this.cacheService = cacheService;
    }

    @Override
    protected ProductService getService() {
        return productService;
    }

    @PostMapping("/")
    @Operation(summary = "Crear producto", description = "Crear un nuevo producto en el sistema de ordenes, tiene validacion de fuerza bruta , de rol de administrador y de sanitizacion de datos")
    @Override
    public ResponseDTO<Product> create(@Valid @RequestBody Product requestBody) {
        if (requestBody.get_id() != null) {
            throw new UnExpectedArgumentError("ID should not be provided when creating a new product.");
        }

        Product newProduct = getService().create(requestBody);

        if (newProduct == null) {
            return new ResponseDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create product", null);
        }

        return new ResponseDTO<>(true, HttpStatus.CREATED.value(), "Product created successfully", newProduct);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto", description = "Obtener un producto por su ID, tiene validacion de fuerza bruta , de rol de administrador o cliente y de cache")
    @Override
    public ResponseDTO<Product> getById(@PathVariable String id ) {

        
        Product product = getService().getById(id);
        CachedData cachedData = new CachedData(200, "Product found", true, product);
        this.cacheService.saveCachedData(this.cacheService.generateCacheKey("GET", "/api/products/", id), cachedData);

        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Product found", product);
    }

    @PostMapping("/query/one")
    @Operation(summary = "Obtener producto", description = "Obtener un producto por un query")
    @Override
    public ResponseDTO<Product> getOne(@Valid @RequestBody CustomQuery requestBody) {
        requestBody.setLimit(1); // Set limit to 1 for single retrieval
        Product product = getService().getOne(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Product found", product);
    }

    

    @PostMapping("/query/many")
    @Operation(summary = "Obtener productos", description = "Obtener productos por un query")
    @Override
    public ResponseDTO<List<Product>> getMany(@Valid @RequestBody CustomQuery requestBody) {
        List<Product> products = getService().getMany(requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Products found", products);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualizar un producto por su ID")
    @Override
    public ResponseDTO<Product> updateOne(@PathVariable String id, @Valid @RequestBody Product requestBody) {
        if (requestBody.get_id() != null) {
            throw new UnExpectedArgumentError("ID should not be provided when updating a product.");
        }
        if (id == null) {
            throw new UnExpectedArgumentError("ID should be provided when updating a product.");
        }
        Product updatedProduct = getService().updateOne(id, requestBody);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Product updated successfully", updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Eliminar un producto por su ID")
    @Override
    public ResponseDTO<Void> deleteOne(@PathVariable String id) {
        getService().deleteOne(id);
        return new ResponseDTO<>(true, HttpStatus.OK.value(), "Product deleted successfully", null);
    }
}