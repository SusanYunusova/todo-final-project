package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.UserDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    ObjectFactory<HttpSession> httpSessionObjectFactory;
    @Autowired
    private HttpServletRequest httpServletRequest;

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
            }
        } catch (Exception e) {
            log.error("error save or update user{}{}", e, e);
        }
        return Optional.empty();
    }

    public String getByEmailAndPassword(String email, String password) {
        try {
            log.info("trying to get by email and password");
           Optional<User> user= userDBService.getByEmailAndPassword(email,password);
                HttpSession session = httpSessionObjectFactory.getObject();
            if (user.isPresent()){
                log.info("user found going to landing..");
                session.setAttribute("user", user.get());
                return "landing";
            }else {

                log.info("user not found by email and password...");
                httpServletRequest.setAttribute("errorMessage","No user found with given criteria");
                return "index";
            }
        }catch (Exception e){
            log.error("error getting user by email and password{}",e,e);
            return "index";
        }

    }

    public String getByEmail(HashMap data) {
        try {

            log.info("trying to get by email");
            Optional<User> user = userDBService.getByEmail((String) data.get("id"));
            if (!user.isPresent()) {
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
            session.setAttribute("user", user.get());
            return "landing";
        } catch (Exception e) {
            log.error("error get By email{}", e, e);
            return "index";
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

    public ResponseEntity<?> getByID(long idUser) {
        try {
            log.info("trying to get  USer by  id");
            Optional<User> byIDUser = userDBService.getById(idUser);
            if (byIDUser.isPresent()) {
                log.info("user has found by id");
                return new ResponseEntity<>(byIDUser.get(), HttpStatus.OK);
            } else {
                log.info("couldn't find by id{}", idUser);
                return new ResponseEntity<>(Optional.empty(), HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error("error get by id o user{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
