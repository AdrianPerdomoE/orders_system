package com.adpe.orders_system.service;

import com.adpe.orders_system.DTO.User;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.error.NotFoundError;
import com.adpe.orders_system.repository.AbstractRepository;
import com.adpe.orders_system.repository.UserMongoRepository;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class UserService extends AbstractService<User> {

    private final AbstractRepository<User> userRepository;

    public UserService(UserMongoRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected AbstractRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    public User create(User entity) {
        return getRepository().create(entity);       
    }

    @Override
    public User getById(String id) {
        User user = getRepository().getById(id);
        if (user == null) {
            throw new NotFoundError("User with ID " + id + " not found");
        }
        return user;
    }

    @Override
    public User getOne(CustomQuery query) {
        User user = getRepository().getOne(query);
        if (user == null) {
            throw new NotFoundError("User not found for query: " + query);
        }
        return user;
    }

    @Override
    public List<User> getMany(CustomQuery query) {
        List<User> users =  getRepository().getMany(query);
        if (users == null || users.isEmpty()) {
            throw new NotFoundError("No users found for query: " + query);
        }
        return users;
    }

    @Override
    public User updateOne(String id, User entity) {
        if (getRepository().getById(id) == null) {
            throw new NotFoundError("User with ID " + id + " not found");
        }
        return getRepository().updateOne(id, entity);
    }

    @Override
    public void deleteOne(String id) {
        if (!getRepository().deleteOne(id)) {
            throw new NotFoundError("User with ID " + id + " not found");
        }
    }
}