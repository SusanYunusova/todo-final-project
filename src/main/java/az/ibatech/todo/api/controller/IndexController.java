package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;

@Controller
@Slf4j
public class IndexController {
    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("index")
    public String indexPage() {
        return "index";
    }
//
@GetMapping("/user")
public String getUser(Principal principal) {
    log.info("loginfb...{}", principal);
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    HashMap data = (HashMap) ((OAuth2Authentication) a).getUserAuthentication().getDetails();
    return userService.getByEmail(data);
}

    @GetMapping("/signUp")
    public String signUp() {
        log.info("going to signup page...");
        return "sign-up";
    }


    @GetMapping("/resetPassword")
    public String resetPassword() {
        log.info("going to resetPassword page...");
        return "reset-password";
    }

    @GetMapping("/signIn")
    public String login(@RequestParam String email, @RequestParam String password){
        log.info("trying to login by email and password");
        return   userService.getByEmailAndPassword(email,password);

    }
}
