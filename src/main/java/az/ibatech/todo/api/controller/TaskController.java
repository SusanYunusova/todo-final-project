package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.TaskService;
import az.ibatech.todo.db.entities.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {
private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/add")
    public ResponseEntity<?> create(@RequestBody Task task){
       log.info("creating task..");
       return taskService.saveOrUpdate(task);
    }
    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody Task task){
        log.info("updating task..");
        return taskService.saveOrUpdate(task);
    }
    @DeleteMapping("/delete/{idTask}")
    public ResponseEntity<?> update (@PathVariable long idTask){
        log.info("deleting task..");
        return taskService.delete(idTask);
    }
    //todo search
    //todo by idUSer list task
    //getByStatus/{status} return listtask
    //todo complite task id ni gonder bu idli taskin icindeki statusu done ele
    //

    @GetMapping("/getById/{idTask}")
    public ResponseEntity<?> getById(@PathVariable long idTask){
        log.info("trying to get task by idTask");
        return taskService.getByID(idTask);
    }
}
