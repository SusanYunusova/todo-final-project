package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.UserDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
public class UserService {
    private final UserDBService userDBService;

    public UserService(UserDBService userDBService) {
        this.userDBService = userDBService;
    }

    public ResponseEntity<?> saveOrUpdate(User user) {
        try {
            log.info("trying to save or update USer");
            Optional<User> savedUser = userDBService.saveUpdate(user);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);

        } catch (Exception e) {
            log.error("error save or update user{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> delete(long idUser) {
        try {
            log.info("trying to delete  USer by  id");
            Optional<User> byIDUser = userDBService.getById(idUser);
            if (byIDUser.isPresent()) {
               userDBService.delete(byIDUser.get());
                return new ResponseEntity<>("deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("couldn't find", HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error("error save or update user{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
