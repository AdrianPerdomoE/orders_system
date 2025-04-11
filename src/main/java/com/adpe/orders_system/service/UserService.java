package com.adpe.orders_system.service;

import com.adpe.orders_system.DTO.CustomUser;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.error.NotFoundError;
import com.adpe.orders_system.repository.AbstractRepository;
import com.adpe.orders_system.repository.UserMongoRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class UserService extends AbstractService<CustomUser> {

    private final AbstractRepository<CustomUser> userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserMongoRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected AbstractRepository<CustomUser> getRepository() {
        return userRepository;
    }

    @Override
    public CustomUser create(CustomUser entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword())); // Encripta la contraseña
        return getRepository().create(entity);       
    }

    @Override
    public CustomUser getById(String id) {
        CustomUser user = getRepository().getById(id);
        if (user == null) {
            throw new NotFoundError("User with ID " + id + " not found");
        }
        return user;
    }

    @Override
    public CustomUser getOne(CustomQuery query) {
        CustomUser user = getRepository().getOne(query);
        if (user == null) {
            throw new NotFoundError("User not found for query: " + query);
        }
        return user;
    }

    @Override
    public List<CustomUser> getMany(CustomQuery query) {
        List<CustomUser> users =  getRepository().getMany(query);
        if (users == null || users.isEmpty()) {
            throw new NotFoundError("No users found for query: " + query);
        }
        return users;
    }

    @Override
    public CustomUser updateOne(String id, CustomUser entity) {
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