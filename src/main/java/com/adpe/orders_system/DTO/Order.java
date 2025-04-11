package com.adpe.orders_system.DTO;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    @Id
    public String _id;
    public String userId;
    public List<OrderItem> products;
    public String status;
    public String createdAt;
    public String updatedAt;
}
