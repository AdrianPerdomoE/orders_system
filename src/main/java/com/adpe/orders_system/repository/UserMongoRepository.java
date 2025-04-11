package com.adpe.orders_system.repository;
import com.adpe.orders_system.DTO.User;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.model.query.CustomQueryFactory;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;
import java.util.List;

@Repository
public class UserMongoRepository extends AbstractRepository<User> {

    private final MongoTemplate mongoTemplate;
    private final CustomQueryFactory queryBuilder;

    public UserMongoRepository(MongoTemplate mongoTemplate, CustomQueryFactory queryBuilder) {
        this.mongoTemplate = mongoTemplate;
        this.queryBuilder = queryBuilder;
    }

    @Override
    protected String getCollectionName() {
        return "users";
    }

    @Override
    protected CustomQueryFactory getQueryBuilder() {
        return this.queryBuilder;
    }

    @Override
    public User create(User entity) {
        return mongoTemplate.insert(entity, getCollectionName());
    }

    @Override
    public User getById(String id) {
        return mongoTemplate.findById(id, User.class, getCollectionName());
    }

    @Override
    public User getOne(CustomQuery query) {
        Query mongoQuery = (Query) getQueryBuilder().buildQuery(query);
        return mongoTemplate.findOne(mongoQuery, User.class, getCollectionName());
    }

    @Override
    public List<User> getMany(CustomQuery query) {
        Query mongoQuery = (Query) queryBuilder.buildQuery(query);
        return mongoTemplate.find(mongoQuery, User.class, getCollectionName());
    }

    @Override
public User updateOne(String id, User entity) {
    Query query = new Query(Criteria.where("_id").is(id));
   Update update = buildUpdateFromEntity(entity);

    mongoTemplate.updateFirst(query, update, User.class, getCollectionName());
    return mongoTemplate.findById(id, User.class, getCollectionName());
}

/**
 * Construye un objeto Update dinámicamente a partir de los campos no nulos de la entidad.
 *
 * @param entity La entidad desde la cual se construirán las actualizaciones.
 * @return Un objeto Update con los campos no nulos.
 */
private Update buildUpdateFromEntity(User entity) {
    Update update = new Update();

    // Usar reflexión para iterar sobre los campos de la clase User
    java.lang.reflect.Field[] fields = entity.getClass().getDeclaredFields();
    for (java.lang.reflect.Field field : fields) {
        field.setAccessible(true); // Permitir acceso a campos privados
        try {
            Object value = field.get(entity); // Obtener el valor del campo
            if (value != null) { // Solo agregar campos que no sean null
                update.set(field.getName(), value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error al acceder al campo: " + field.getName(), e);
        }
    }

    return update;
}
    @Override
    public boolean deleteOne(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, User.class, getCollectionName());
        return result.getDeletedCount() > 0;
    }
}
