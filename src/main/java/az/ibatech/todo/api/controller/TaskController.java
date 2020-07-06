package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.TaskService;
import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
//@RequestMapping("/task")
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private  final UserService userService;
    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/createTask")
    public String login(
                        @RequestParam String deadline,
                        @RequestParam String taskName,
                        @RequestParam String description) throws ParseException {
        log.info("trying to add task ");
        HttpSession session = httpSessionObjectFactory.getObject();
        User user = (User) session.getAttribute("user");
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(deadline);
        Task task = Task.builder()
                .createdTime(new Date())
                .deadline(date1)
                .taskName(taskName)
                .description(description)
                .idUser(user)
                .build();
        log.info("created task:{}",task.getTaskName());
        taskService.saveOrUpdate(task);
        User newUser = (User) userService.getByID(user.getIdUser()).getBody();
        session.setAttribute("user",newUser);
        session.setAttribute("task",task);
        return "tasks-dashboard";

    }

//    @GetMapping("/add")
//    public ResponseEntity<?> create(@RequestBody Task task) {
//        log.info("creating task..");
//        return taskService.saveOrUpdate(task);
//    }
//
//    @PutMapping("update")
//    public ResponseEntity<?> update(@RequestBody Task task) {
//        log.info("updating task..");
//        return taskService.saveOrUpdate(task);
//    }
//
//    @DeleteMapping("/delete/{idTask}")
//    public ResponseEntity<?> update(@PathVariable long idTask) {
//        log.info("deleting task..");
//        return taskService.delete(idTask);
//    }
    //todo search
    //todo by idUSer list task
    //todo getByStatus/{status} return listtask
    //todo complite task id ni gonder bu idli taskin icindeki statusu done ele
    //todo idsi loggedun idsi olan statusu done olan isDelete 0 tasklarn listi

//    @GetMapping("/getById/{idTask}")
//    public ResponseEntity<?> getById(@PathVariable long idTask) {
//        log.info("trying to get task by idTask");
//        return taskService.getByID(idTask);
//    }


    //    int status (default-0,deleted-1,overdue-2,today-3,done-4)
    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable long status) {
        log.info("trying to get taskList by status");
        return taskService.getByStatus(status);
    }

    @GetMapping("/complete/{idTask}")
    public ResponseEntity<?> complete(@PathVariable long idTask) {
        log.info("trying to get complete by idTask");
        return taskService.complete(idTask);
    }


}
