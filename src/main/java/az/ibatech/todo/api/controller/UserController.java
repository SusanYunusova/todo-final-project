package az.ibatech.todo.api.controller;

import az.ibatech.todo.api.service.NotificationService;
import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.db.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @GetMapping("api/createUser")
    public String login(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {
        log.info("trying to create new User fullName: {} email:{} password:{} ",fullName,email,password);
        return userService.createUser(email, fullName, password, model);
    }

    @GetMapping("api/mail")
    public String mail(@RequestParam String email, HttpSession session, Model model) {
        log.info("trying to update password  User ");
        boolean result = notificationService.sendNotificationReset(email, session);
        if (result) {
            return "index";
        } else {
            model.addAttribute("wrongMail", "User not found with given criteria,please write correct mail");
            return "reset-password";
        }

    }

    @GetMapping("api/new-password/{token}")
    public String newPassword(@PathVariable String token, HttpSession session) {
        boolean result = userService.resetPassword(token, session);
        if (result) {
            return "new-password";
        } else {
            return "error";
        }
    }

//    @GetMapping("api/confirmMail")
//    public String confirmMail(@RequestParam String email, HttpSession session, Model model) {
//        log.info("trying to get confirm by email:{} ",email);
//        boolean result = notificationService.sendNotificationForConfirm(email, session);
//        if (result) {
//            return "confirm-user";
//        } else {
//            model.addAttribute("wrongMail", "Please write valid email");
//            return "sign-up";
//        }
//
//    }

    @GetMapping("api/confirm/{token}")
    public String confirmUser(@PathVariable String token, HttpSession session) {
        boolean result = userService.confirm(token, session);
        if (result) {
            return "landing";
        } else {
            return "error";
        }
    }

    //todo bunu service de et
    @GetMapping("/api/updatePassword")
    public String updatePassword(@RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        if (password.equals(confirmPassword)) {
            User user = (User) session.getAttribute("user");
            user.setPassword(password);
            userService.updatePassword(user);
            return "index";
        } else {
            model.addAttribute("wrongPassword", "passwords didnt match,please try again");
            return "new-password";
        }
    }

    @PostMapping("/add")
    public void create(@RequestBody User user) {
        log.info("creating user...");
        userService.saveOrUpdate(user);

    }

    @PutMapping("/update")
    public void update(@RequestBody User user) {
        log.info("updating user...");
        userService.saveOrUpdate(user);

    }

    @DeleteMapping("/delete/{idUser}")
    public ResponseEntity<?> update(@PathVariable long idUser) {
        log.info("deleting user...");
        return userService.delete(idUser);

    }

    @GetMapping("/getById/{idUser}")
    public ResponseEntity<?> getById(@PathVariable long idUser) {
        log.info("trying to get user by idUser");
        return userService.getByID(idUser);
    }
}
