package ru.itis.dao;

public interface CrudDAO<M, I> {
    void save(M model);
    M find(I id);
    void delete(I id);
    void update(M model, I id);
}
