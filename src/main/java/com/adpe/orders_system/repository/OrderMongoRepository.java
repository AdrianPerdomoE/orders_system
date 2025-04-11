package com.adpe.orders_system.repository;

import com.adpe.orders_system.DTO.Order;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.model.query.CustomQueryFactory;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderMongoRepository extends AbstractRepository<Order> {

    private final MongoTemplate mongoTemplate;
    private final CustomQueryFactory queryBuilder;

    public OrderMongoRepository(MongoTemplate mongoTemplate, CustomQueryFactory queryBuilder) {
        this.mongoTemplate = mongoTemplate;
        this.queryBuilder = queryBuilder;
    }

    @Override
    protected String getCollectionName() {
        return "orders"; // Nombre de la colección en MongoDB
    }

    @Override
    protected CustomQueryFactory getQueryBuilder() {
        return this.queryBuilder;
    }

    @Override
    public Order create(Order entity) {
        return mongoTemplate.insert(entity, getCollectionName());
    }

    @Override
    public Order getById(String id) {
        return mongoTemplate.findById(id, Order.class, getCollectionName());
    }

    @Override
    public Order getOne(CustomQuery query) {
        Query mongoQuery = (Query) getQueryBuilder().buildQuery(query);
        return mongoTemplate.findOne(mongoQuery, Order.class, getCollectionName());
    }

    @Override
    public List<Order> getMany(CustomQuery query) {
        Query mongoQuery = (Query) queryBuilder.buildQuery(query);
        return mongoTemplate.find(mongoQuery, Order.class, getCollectionName());
    }

    @Override
    public Order updateOne(String id, Order entity) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = buildUpdateFromEntity(entity);

        mongoTemplate.updateFirst(query, update, Order.class, getCollectionName());
        return mongoTemplate.findById(id, Order.class, getCollectionName());
    }

    @Override
    public boolean deleteOne(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, Order.class, getCollectionName());
        return result.getDeletedCount() > 0;
    }

    /**
     * Construye un objeto Update dinámicamente a partir de los campos no nulos de la entidad.
     *
     * @param entity La entidad desde la cual se construirán las actualizaciones.
     * @return Un objeto Update con los campos no nulos.
     */
    private Update buildUpdateFromEntity(Order entity) {
        Update update = new Update();

        // Usar reflexión para iterar sobre los campos de la clase Order
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
}