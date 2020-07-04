package az.ibatech.todo.db.repos;

import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findByIdTask(long idTask);

    List<Task> findAllByIdUser(long idUser);//todo long idUSer olmali deil?

    List<Task> findAllByStatus(long status);
}
