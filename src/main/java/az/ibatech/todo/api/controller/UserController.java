package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

/**
 * localhost:2020/user
 */
@Controller
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/createUser")
    public String login(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password)  {
        log.info("trying to create new User ");
        User user = User.builder()
                .email(email)
                .fullName(fullName)
                .password(password)
                .build();
        log.info("created user:{}",user.getFullName());
        userService.saveOrUpdate(user);
        return "index";

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
