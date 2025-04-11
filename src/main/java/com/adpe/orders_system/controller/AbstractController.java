package com.adpe.orders_system.controller;

import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.ResponseDTO;
import com.adpe.orders_system.service.AbstractService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class AbstractController<T> {

    protected abstract AbstractService<T> getService();

    public abstract ResponseDTO<T> create(@Valid @RequestBody T requestBody);

    public abstract ResponseDTO<T> getById(@PathVariable String id);

    public abstract ResponseDTO<T> getOne(@Valid @RequestBody CustomQuery requestBody);

    public abstract ResponseDTO<List<T>> getMany(@Valid @RequestBody CustomQuery requestBody);

    public abstract ResponseDTO<T> updateOne(@PathVariable String id, @Valid @RequestBody T requestBody);

    public abstract ResponseDTO<Void> deleteOne(@PathVariable String id);
}