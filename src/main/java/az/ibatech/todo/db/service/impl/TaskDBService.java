package az.ibatech.todo.db.service.impl;

import az.ibatech.todo.db.service.MySqlDBService;

import java.util.List;
import java.util.Optional;

public class TaskDBService implements MySqlDBService {
    @Override
    public Optional saveUpdate(Object o) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Object o) {
        return false;
    }

    @Override
    public Optional getById(long id) {
        return Optional.empty();
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public List getAllBy(long id) {
        return null;
    }
}
