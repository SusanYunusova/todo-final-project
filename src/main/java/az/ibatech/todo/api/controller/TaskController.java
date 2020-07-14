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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("api/createTask")
    public String login(
            @RequestParam String deadline,
            @RequestParam String taskName,
            @RequestParam String description) throws ParseException {
        log.info("trying to add task ");
        HttpSession session = httpSessionObjectFactory.getObject();
        User user = (User) session.getAttribute("user");
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(deadline);
        Task task = Task.builder()
                .createdTime(new Date())
                .deadline(date1)
                .taskName(taskName)
                .description(description)
                .idUser(user)
                .build();
        log.info("created task:{}", task.getTaskName());
        taskService.saveOrUpdate(task, session);
        User newUser = (User) userService.getByID(user.getIdUser());
        session.setAttribute("user", newUser);
        session.setAttribute("task", task);
        return "tasks-dashboard";

    }

    @GetMapping("api/taskEdit/{idTask}")
    public String editTask(@PathVariable long idTask, Model model) {
        Task task = taskService.getByID(idTask).getBody().get();
        model.addAttribute("taskForEdit", task);
        return "tasks-dashboard";
    }

    @GetMapping("api/updateTask")
    public String updateTask(
            @RequestParam long idTask,
            @RequestParam String description,
            @RequestParam String deadline,
            @RequestParam String taskName, HttpSession session) throws ParseException {
        log.info("trying to update taskk by id Task ");
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "index";
        }
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(deadline);
        Task task = Task.builder()
                .idUser(user)
                .idTask(idTask)
                .description(description)
                .taskName(taskName)
                .deadline(date1)
                .build();
        log.info("created task:{}", task.getTaskName());
        taskService.saveOrUpdate(task, session);
        return "tasks-dashboard";

    }

    @GetMapping("api/complete/{idTask}")
    public String complete(@PathVariable long idTask, HttpSession session) {
        log.info("trying to get complete by idTask");
        taskService.complete(idTask);

        return taskService.getTaskByCurrentStatus(session);
    }

    @GetMapping("api/sendArchive/{idTask}")
    public String sendArchive(@PathVariable long idTask, HttpSession session) {
        log.info("trying to  sendArchive by idTask");
        taskService.sendArchive(idTask);
        return taskService.getTaskByCurrentStatus(session);
    }

    @GetMapping("api/deletePopUp/")
    public String deletePopUp( Model model) {
        log.info("trying to  deletePopUp by idTask");
//        taskService.sendArchive(idTask);
        model.addAttribute("deletePopUp", "Are you sure to delete?\nThis action can not be undone");
        return "tasks-archive";
    }

    @GetMapping("api/deleteTask/{idTask}")
    public String deleteTask(@PathVariable int idTask, HttpSession session,Model model) {
        boolean deleted = taskService.delete(idTask);
        if (deleted){
            log.info("deleted task succesfully");
            model.addAttribute("deletedTask","Task deleted successfully");
            taskService.getTaskByCurrentStatus(session);
            return "tasks-archive";
        }else {
            log.info("couldnt delete");
            model.addAttribute("errorDelete","Something went wrong...couldn't delete..");
            return "tasks-archive";
        }
    }
    @GetMapping("api/sort-tasks/{status}")
    public String showTasksByStatus(@PathVariable int status, HttpSession session) {
        return taskService.getTaskByStatus(status, session);
    }

    @GetMapping("api/task-archive/{status}")
    public String taskArchive(@PathVariable int status, HttpSession session) {
        return taskService.getTaskArchive(status, session);
    }

}
