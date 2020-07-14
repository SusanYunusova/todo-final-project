package az.ibatech.todo.db.repos;

import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  TaskRepository extends CrudRepository<Task,Long> {
    Optional<Task> findByIdTaskAndIsDelete(long idTask, int isDelete);

    List<Task> findAllByIdUserAndIsDelete(User idUser, int isDelete);//todo long idUSer olmali deil?

    List<Task> findAllByStatusAndIdUserAndIsDelete(int status, User idUser, int isDelete);

    List<Task> findAllByIsDelete(int isDelete);
}
