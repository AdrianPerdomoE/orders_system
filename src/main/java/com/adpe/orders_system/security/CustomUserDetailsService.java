package com.adpe.orders_system.security;
import com.adpe.orders_system.model.query.Operator;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.adpe.orders_system.repository.UserMongoRepository;
import  com.adpe.orders_system.repository.AbstractRepository;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.CustomUser;
import com.adpe.orders_system.error.NotFoundError;
import com.adpe.orders_system.model.query.QueryProperty;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AbstractRepository<CustomUser> userRepository;

    public CustomUserDetailsService(UserMongoRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos
        CustomQuery query = new CustomQuery();
        QueryProperty queryProperty = new QueryProperty("name",Operator.EQUALS, username);
        query.getProperties().add(queryProperty);
        CustomUser userEntity = userRepository.getOne(query);
        if (userEntity == null) {
            throw new NotFoundError(username + " not found"); // Lanza una excepción si no se encuentra el usuario
            
        }

        // Convierte el usuario de la base de datos a un objeto UserDetails
        return User.builder()
                .username(userEntity.getName())
                .password(userEntity.getPassword()) // Contraseña encriptada
                .roles(userEntity.getRol()) // Asigna los roles
                .build();
    }
}