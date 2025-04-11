package com.adpe.orders_system.service;

import com.adpe.orders_system.DTO.Product;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.error.NotFoundError;
import com.adpe.orders_system.repository.AbstractRepository;
import com.adpe.orders_system.repository.ProductMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService extends AbstractService<Product> {

    private final AbstractRepository<Product> productRepository;

    public ProductService(ProductMongoRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected AbstractRepository<Product> getRepository() {
        return productRepository;
    }

    @Override
    public Product create(Product entity) {
        return getRepository().create(entity);
    }

    @Override
    public Product getById(String id) {
        Product product = getRepository().getById(id);
        if (product == null) {
            throw new NotFoundError("Product with ID " + id + " not found");
        }
        return product;
    }

    @Override
    public Product getOne(CustomQuery query) {
        Product product = getRepository().getOne(query);
        if (product == null) {
            throw new NotFoundError("Product not found for query: " + query);
        }
        return product;
    }

    @Override
    public List<Product> getMany(CustomQuery query) {
        List<Product> products = getRepository().getMany(query);
        if (products == null || products.isEmpty()) {
            throw new NotFoundError("No products found for query: " + query);
        }
        return products;
    }

    @Override
    public Product updateOne(String id, Product entity) {
        if (getRepository().getById(id) == null) {
            throw new NotFoundError("Product with ID " + id + " not found");
        }
        return getRepository().updateOne(id, entity);
    }

    @Override
    public void deleteOne(String id) {
        if (!getRepository().deleteOne(id)) {
            throw new NotFoundError("Product with ID " + id + " not found");
        }
    }
}