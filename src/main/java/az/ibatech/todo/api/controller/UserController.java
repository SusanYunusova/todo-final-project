package az.ibatech.todo.api.controller;

import az.ibatech.todo.db.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private  final  UserService userService;

    @GetMapping("/add")
    public ResponseEntity<?>  create(User user){
       log.info("creating user...");
      return userService.saveOrUpdate(user);

    }

    @PutMapping("/update")
    public ResponseEntity<?>  update(User user){
       log.info("updating user...");
      return userService.saveOrUpdate(user);

    }
    @DeleteMapping("/delete")
    public ResponseEntity<?>  update(User user){
       log.info("creating user...");
      return userService.saveOrUpdate(user);

    }
}
