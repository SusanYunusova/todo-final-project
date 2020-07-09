package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.NotificationService;
import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.db.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * localhost:2020/user
 */
@Controller
@Slf4j
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;

    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
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
    @GetMapping("/mail")
    public String mail()  {
        log.info("trying to create new User ");
        User user = User.builder()
                .email("yunusova96.ys@gmail.com")
                .fullName("Susan Yunusova")
                .password("123")
                .build();
        try {
            log.info("yaradilmish useri maile gonderrem ");
            notificationService.sendNotification(user);
        }catch (MailException e){
            log.error("mail send error{}",e,e);

        }
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
