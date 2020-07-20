package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LandingController {
    private  final TaskService taskService;

    public LandingController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("api/dashboard")
    public String dashboard(HttpSession session, Model model) {
        log.info("going to dashboard page...");
        return taskService.getTaskByCurrentStatus(session, model);
    }
}
