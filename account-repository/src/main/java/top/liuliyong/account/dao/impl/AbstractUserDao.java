package top.liuliyong.account.dao.impl;

import top.liuliyong.account.dao.IMongoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


/**
 * @Author liyong.liu
 * @Date 2019/3/11
 **/
public abstract class AbstractUserDao<T> implements IMongoService<T> {
    @Autowired
    MongoOperations mongoTemplate;

    protected abstract Class<T> getEntityClass();

    protected abstract String getCollection();

    @Override
    public <S extends T> S saveUser(S entity) {
        return mongoTemplate.save(entity, getCollection());
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.of(mongoTemplate.findById(new ObjectId(id), getEntityClass(), getCollection()));
    }

    @Override
    public Iterable<T> findAll() {
        return mongoTemplate.findAll(getEntityClass(), getCollection());
    }

    @Override
    public long count() {
        return mongoTemplate.count(query(where("id").exists(true)), getEntityClass(), getCollection());
    }

}
