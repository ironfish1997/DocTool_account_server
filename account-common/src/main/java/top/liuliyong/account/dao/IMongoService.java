package top.liuliyong.account.dao;


import java.util.List;
import java.util.Optional;

public interface IMongoService<T> {

    <S extends T> S saveUser(S entity);

    Optional<T> findById(String id);

    Iterable<T> findAll();

    long count();

    List deleteUser(String... ids);

    <S extends T> S updateUser(S entity);
}
