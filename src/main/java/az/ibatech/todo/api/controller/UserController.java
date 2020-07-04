package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.db.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

/**
 * localhost:2020/user
 */
@Controller
//@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("index")
    public String indexPage() {
        return "index";
    }

    @GetMapping("addTask")
    public String adTask() {
        return "add-task";
    }


    @GetMapping("/user")
    public String getUser(Principal principal) {
        log.info("loginfb...{}", principal);
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        HashMap data = (HashMap) ((OAuth2Authentication) a).getUserAuthentication().getDetails();
        return userService.getByEmail(data);
    }

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody User user) {
        log.info("creating user...");
//      return userService.saveOrUpdate(user);
        return null;

    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user) {
        log.info("updating user...");
//      return userService.saveOrUpdate(user);
        return null;
    }

    @DeleteMapping("/delete/{idUser}")
    public ResponseEntity<?> update(@PathVariable long idUser) {
        log.info("deleting user...");
        return userService.delete(idUser);

    }

    //todo search

    @GetMapping("/getById/{idUser}")
    public ResponseEntity<?> getById(@PathVariable long idUser) {
        log.info("trying to get user by idUser");
        return userService.getByID(idUser);
    }
}
