package az.ibatech.todo.db.repos;

import az.ibatech.todo.db.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findByIdTask(long idTask);

}
