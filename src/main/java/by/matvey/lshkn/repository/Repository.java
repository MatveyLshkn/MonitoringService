package by.matvey.lshkn.repository;


import java.util.Optional;

/**
 * Base interface for repositories
 */
public interface Repository<T, U> {
    T save(T object);

    Optional<T> findById(U objectId);

    boolean update(T object);

    boolean delete(T object);
}
