package com.adpe.orders_system.DTO;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @Id
    public String _id;
    public String name;
    public String description;
    public String sku;
    public int stock;
    public double price;

    public Product(String name, String description, String sku, int stock, double price) {
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.stock = stock;
        this.price = price;
    }
}
