package com.adpe.orders_system.DTO;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @Id
    public String _id;
    public String name;
    public String description;
    public String sku;
    public int stock;
    public double price;
}
