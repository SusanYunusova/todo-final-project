package az.ibatech.todo.db.repos;

import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  TaskRepository extends CrudRepository<Task,Long> {
    Optional<Task> findByIdTaskAndIsDelete(long idTask, int isDelete);

    List<Task> findAllByIdUserAndIsDelete(User idUser, int isDelete, Pageable pagination);

    List<Task> findAllByStatusAndIdUserAndIsDelete(int status, User idUser, int isDelete, Pageable pagination);

    List<Task> findAllByIsDelete(int isDelete);
    int countAllByIdUserAndIsDeleteAndStatus(User idUser ,int isDelete,int status);
    int countAllByIdUserAndIsDelete(User idUser ,int isDelete);
}
