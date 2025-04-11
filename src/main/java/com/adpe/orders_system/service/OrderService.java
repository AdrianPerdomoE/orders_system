package com.adpe.orders_system.service;

import com.adpe.orders_system.DTO.Order;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.error.NotFoundError;
import com.adpe.orders_system.repository.AbstractRepository;
import com.adpe.orders_system.repository.OrderMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService extends AbstractService<Order> {

    private final AbstractRepository<Order> orderRepository;

    public OrderService(OrderMongoRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected AbstractRepository<Order> getRepository() {
        return orderRepository;
    }

    @Override
    public Order create(Order entity) {
        return getRepository().create(entity);
    }

    @Override
    public Order getById(String id) {
        Order order = getRepository().getById(id);
        if (order == null) {
            throw new NotFoundError("Order with ID " + id + " not found");
        }
        return order;
    }

    @Override
    public Order getOne(CustomQuery query) {
        Order order = getRepository().getOne(query);
        if (order == null) {
            throw new NotFoundError("Order not found for query: " + query);
        }
        return order;
    }

    @Override
    public List<Order> getMany(CustomQuery query) {
        List<Order> orders = getRepository().getMany(query);
        if (orders == null || orders.isEmpty()) {
            throw new NotFoundError("No orders found for query: " + query);
        }
        return orders;
    }

    @Override
    public Order updateOne(String id, Order entity) {
        if (getRepository().getById(id) == null) {
            throw new NotFoundError("Order with ID " + id + " not found");
        }
        return getRepository().updateOne(id, entity);
    }

    @Override
    public void deleteOne(String id) {
        if (!getRepository().deleteOne(id)) {
            throw new NotFoundError("Order with ID " + id + " not found");
        }
    }
}