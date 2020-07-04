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
    //todo getByStatus/{status} return listtask
    //todo complite task id ni gonder bu idli taskin icindeki statusu done ele
    //todo idsi loggedun idsi olan statusu done olan isDelete 0 tasklarn listi

    @GetMapping("/getById/{idTask}")
    public ResponseEntity<?> getById(@PathVariable long idTask){
        log.info("trying to get task by idTask");
        return taskService.getByID(idTask);
    }
    @GetMapping("/getByIdUser/{idUser}")
    public ResponseEntity<?> getByIdUser(@PathVariable long idUser){
        log.info("trying to get taskList by idUser");
        return taskService.getByIDUser(idUser);
    }

//    int status (default-0,deleted-1,overdue-2,today-3,done-4)
    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable long status){
        log.info("trying to get taskList by status");
        return taskService.getByStatus(status);
    }
    @GetMapping("/complete/{idTask}")
    public ResponseEntity<?> complete(@PathVariable long idTask){
        log.info("trying to get complete by idTask");
        return taskService.complete(idTask);
    }
//same with get by status
//    @GetMapping("/archive/{idUser}")
//    public ResponseEntity<?> getArchive(@PathVariable long idUser){
//        log.info("trying to get archieve by idUser");
//        return taskService.getArchive(idUser);
//    }

}
