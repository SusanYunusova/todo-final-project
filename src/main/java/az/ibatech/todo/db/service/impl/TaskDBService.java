package az.ibatech.todo.db.service.impl;

import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.repos.TaskRepository;
import az.ibatech.todo.db.service.MySqlDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TaskDBService implements MySqlDBService<Task> {
    private final TaskRepository taskRepository;

    public TaskDBService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }




    @Override
    public Optional<Task> saveUpdate(Task task) {
        try {
            log.info("trying to save task to db");
            taskRepository.save(task);
            return Optional.of(task);

        } catch (Exception e) {
            log.error("error saving task to db {}{}", e, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Task task) {
        try {
            log.info("trying to delete task from db   id:{}",task.getIdTask());
            task.setIsDelete(1);
            taskRepository.save(task);
            log.info("list of all by idUSer{}",task);
            return true;
        } catch (Exception e) {
            log.error("error from deleting task from db{}{}", e, e);
            return false;
        }
    }

    @Override
    public Optional<Task> getById(long id) {
        try {
            log.info("trying to get task by id from db");
            return taskRepository.findByIdTaskAndIsDelete(id,0);
        } catch (Exception e) {
            log.error("error getting by id from Db{}{}", e, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Task> getAll() {
        try {
            log.info("trying to getAll task from db");
            return taskRepository.findAllByIsDelete(0);
        } catch (Exception e) {
            log.error("error getting all from db{}{}", e, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Task> getAllBy(long id) {
        return null;
    }

    public List<Task> getByIdUser(User idUser,int page,int count) {
        try {
            log.info("trying to getAll taskList from db by idUser");
            Pageable pagination = PageRequest.of(page, count);
            return taskRepository.findAllByIdUserAndIsDelete(idUser,0,pagination);
        } catch (Exception e) {
            log.error("error getting all from db by idUser{}{}", e, e);
            return new ArrayList<>();
        }
    }
    public  int getTaskCountByStatus(User user,int status){
        try {
            log.info("getting task count by status user:{},status:{}",user.getFullName(),status);
           return status!=0? taskRepository.countAllByIdUserAndIsDeleteAndStatus(user,0,status)
                   :taskRepository.countAllByIdUserAndIsDelete(user,0);

        }catch (Exception e){
            log.error("error getting count of task by status error{}",e,e);
            return 0;
        }
    }

//    public List<Task> getByStatus(int status,User idUser) {
//        try {
//            log.info("trying to getAll taskList from db by status");
//
//            return taskRepository.findAllByStatusAndIdUserAndIsDelete(status,idUser,0, pagination);
//        } catch (Exception e) {
//            log.error("error getting all from db by status{}{}", e, e);
//            return new ArrayList<>();
//        }
//    }
    public List<Task> getByStatus(int status,User idUser,int page, int count) {
        try {
            log.info("trying to getAll taskList from db by status");
            Pageable pagination = PageRequest.of(page, count);
            return taskRepository.findAllByStatusAndIdUserAndIsDelete(status,idUser,0,pagination);
        } catch (Exception e) {
            log.error("error getting all from db by status{}{}", e, e);
            return new ArrayList<>();
        }
    }

    public Optional<Task> complete(long idTask) {
        try {
            log.info("trying to get task by idTask{}", idTask);
            Optional<Task> byIdTask = taskRepository.findByIdTaskAndIsDelete(idTask,0);
            if (byIdTask.isPresent()) {
                log.info("setting status to complete 4");
                byIdTask.get().setStatus(4);
                log.info("updating task");
                return saveUpdate(byIdTask.get());
            } else {
                log.info("task by id not found");
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("error updating complete by idTask{}{}", e, e);
            return Optional.empty();
        }
    }
    public Optional<Task> sendArchive(long idTask) {
        try {
            log.info("trying to get task by idTask{}", idTask);
            Optional<Task> byIdTask = taskRepository.findByIdTaskAndIsDelete(idTask,0);
            if (byIdTask.isPresent()) {
                log.info("setting status to sendArchive 1");
                byIdTask.get().setStatus(1);
                log.info("updating task");
                return saveUpdate(byIdTask.get());
            } else {
                log.info("task by id not found");
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("error updating sendArchive by idTask{}{}", e, e);
            return Optional.empty();
        }
    }
}
