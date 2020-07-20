package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.TaskService;
import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            Model model,
            @RequestParam String deadline,
            @RequestParam String taskImage,
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
                .imageUrl(taskImage)
                .description(description)
                .idUser(user)
                .build();
        log.info("created task:{}", task.getTaskName());
        taskService.saveOrUpdate(task, session, model);
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
            Model model,
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
        taskService.saveOrUpdate(task, session, model);
        return "tasks-dashboard";

    }

    @GetMapping("api/complete/{idTask}")
    public String complete(@PathVariable long idTask, HttpSession session, Model model) {
        log.info("trying to get complete by idTask");
        taskService.complete(idTask);

        return taskService.getTaskByCurrentStatus(session, model);
    }

    @GetMapping("api/sendArchive/{idTask}")
    public String sendArchive(@PathVariable long idTask, HttpSession session, Model model) {
        log.info("trying to  sendArchive by idTask");
        taskService.sendArchive(idTask);
        return taskService.getTaskByCurrentStatus(session, model);
    }

    @GetMapping("api/deletePopUp/{idTask}")
    public String deletePopUp(@PathVariable long idTask, Model model) {
        log.info("trying to  deletePopUp by idTask");
//        taskService.sendArchive(idTask);
        model.addAttribute("deletePopUp", "Are you sure to delete?\nThis action can not be undone");
        model.addAttribute("idTaskForDelete", idTask);
        return "tasks-archive";
    }

    @GetMapping("api/deleteTask/{idTask}")
    public String deleteTask(@PathVariable int idTask, HttpSession session, Model model) {
        boolean deleted = taskService.delete(idTask);
        if (deleted) {
            log.info("deleted task succesfully");
            model.addAttribute("deletedTask", "Task deleted successfully");
            taskService.getTaskByCurrentStatus(session, model);
            return "tasks-archive";
        } else {
            log.info("couldnt delete");
            model.addAttribute("errorDelete", "Something went wrong...couldn't delete..");
            return "tasks-archive";
        }
    }

    //
    @GetMapping("api/sort-tasks/{status}")
    public String showTasksByStatus(@PathVariable int status, HttpSession session, Model model) {
        return taskService.getTaskByStatus(status, session, model);
    }

    @GetMapping("api/sort-tasks/pagination")
    public String showTasksByStatusPagination(@RequestParam int status, HttpSession session,
                                              Model model,
                                              @RequestParam("page") Optional<Integer> page
    ) {
        session.setAttribute("current", page.get()-1);

        return taskService.getTaskByStatus(status, session, model);

//        Page<Task> taskPage = taskService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
//
//        model.addAttribute("taskPage", taskPage);
//
//        int totalPages = taskPage.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                    .boxed()
//                    .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
//
//        return taskService.getTaskByStatus(status, session);

    }


    @GetMapping("api/task-archive/{status}")
    public String taskArchive(@PathVariable int status, HttpSession session, Model model) {
        return taskService.getTaskArchive(status, session, model);
    }

    @GetMapping("api/exitArchive")
    public String exitArchive() {
        log.info("exitArchive....");
        return "tasks-archive";
    }

}
