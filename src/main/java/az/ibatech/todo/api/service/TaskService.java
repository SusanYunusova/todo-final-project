package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.TaskDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Slf4j
public class TaskService {
    private final TaskDBService taskDBService;

    public TaskService(TaskDBService taskDBService) {
        this.taskDBService = taskDBService;
    }

    public ResponseEntity<?> saveOrUpdate(Task task) {
        try {
            log.info("trying to save or update Task");
            Optional<Task> savedTask = taskDBService.saveUpdate(task);
            return new ResponseEntity<>(savedTask, HttpStatus.OK);

        } catch (Exception e) {
            log.error("error save or update task{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> delete(long id) {
        try {
            log.info("trying to delete  Task by  id");
            Optional<Task> byIDTask = taskDBService.getById(id);
            if (byIDTask.isPresent()) {
                taskDBService.delete(byIDTask.get());
                return new ResponseEntity<>("deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("couldn't find", HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error("error save or update task{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getByID(long id) {
        try {
            log.info("trying to get  USer by  id");
            Optional<Task> byIDTask = taskDBService.getById(id);
            if (byIDTask.isPresent()) {
                log.info("task has found by id");
                return new ResponseEntity<>(byIDTask, HttpStatus.OK);
            } else {
                log.info("couldn't find by id{}",id);
                return new ResponseEntity<>(Optional.empty(), HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error("error get by id o task{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
