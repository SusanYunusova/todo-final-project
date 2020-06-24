package az.ibatech.todo.db.dao;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface Dao<A>{
    Optional<A> getById(int id);
    Collection<A> getAllById(int id);
    Collection<A> getAll();
    Collection<A> getAllBy(Predicate<A> p);
    A create(A data);
    boolean update(A a);
    boolean delete(int id);
}
