package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class DashController {
    private final TaskService taskService;

    public DashController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/addTask")
    public String adTask() {
        return "add-task";
    }
}
