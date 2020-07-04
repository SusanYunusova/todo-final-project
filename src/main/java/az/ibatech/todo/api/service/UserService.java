package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.UserDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;

    private final UserDBService userDBService;

    public UserService(UserDBService userDBService) {
        this.userDBService = userDBService;
    }

    public Optional<User> saveOrUpdate(User user) {
        try {
            log.info("trying to get by email");
            Optional<User> byEmail = userDBService.getByEmail(user.getEmail());
            if (!byEmail.isPresent()) {
                log.info("trying to save or update USer");
                Optional<User> savedUser = userDBService.saveUpdate(user);
                return savedUser;
//                return new ResponseEntity<>(savedUser, HttpStatus.OK);
            }else {
//                return new ResponseEntity<>(Optional.empty(),HttpStatus.ALREADY_REPORTED);
            }
        } catch (Exception e) {
            log.error("error save or update user{}{}", e, e);
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
//        return user;
    }



    public String getByEmail(HashMap data){
        log.info("trying to get by email");
        Optional<User> user = userDBService.getByEmail((String)data.get("id"));
        if(!user.isPresent()){
            log.info("user not found tryint to insert");
            user = saveOrUpdate(
                    User
                            .builder()
                            .email((String) data.get("id"))
                            .fullName((String) data.get("name"))
                            .password((String) data.get("id"))
                            .build()
            );

        }
        HttpSession session = httpSessionObjectFactory.getObject();
        session.setAttribute("user",user);
        return "landing";
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

    public ResponseEntity<?> getByID(long idUser) {
        try {
            log.info("trying to get  USer by  id");
            Optional<User> byIDUser = userDBService.getById(idUser);
            if (byIDUser.isPresent()) {
                log.info("user has found by id");
                return new ResponseEntity<>(byIDUser, HttpStatus.OK);
            } else {
                log.info("couldn't find by id{}",idUser);
                return new ResponseEntity<>(Optional.empty(), HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error("error get by id o user{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
