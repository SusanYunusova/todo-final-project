package az.ibatech.todo.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LandingController {


    @GetMapping("api/dashboard")
    public String dashboard() {
        log.info("going to dashboard page...");
        return "tasks-dashboard";
    }
}
