package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.UserDBService;
import az.ibatech.todo.utility.GenerateCustomToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

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
    private  final GenerateCustomToken generateCustomToken;
    private final UserDBService userDBService;
    private final NotificationService notificationService;

    public UserService(GenerateCustomToken generateCustomToken, UserDBService userDBService, NotificationService notificationService) {
        this.generateCustomToken = generateCustomToken;
        this.userDBService = userDBService;
        this.notificationService = notificationService;
    }

    public String createUser(String email, String fullName, String password, Model model){
        Optional<User> byEmail = userDBService.getByEmail(email);
        if (byEmail.isPresent()){
            log.info("user found with email {}",email);
            model.addAttribute("repeatedMail","User with this email already exist,please try another one");
            return "sign-up";

        }else {
            log.info("creating new user by given criteria,mail:{}",email);
            User user = User.builder()
                    .password(password)
                    .fullName(fullName)
                    .email(email)
                    .isConfirm(0)
                    .token(generateCustomToken.generateToken())
                    .build();
            userDBService.saveUpdate(user);
            notificationService.sendMailForConfirm(user);
        }

        return "waiting";
    }

    public Optional<User> saveOrUpdate(User user) {
        try {
            log.info("trying to get by email");
            Optional<User> byEmail = userDBService.getByEmail(user.getEmail());
            if (!byEmail.isPresent()) {
                log.info("trying to save or update USer");
                Optional<User> savedUser = userDBService.saveUpdate(user);
                return savedUser;
            }else {

            }
        } catch (Exception e) {
            log.error("error save or update user{}{}", e, e);
        }
        return Optional.empty();
    }
    public void updatePassword(User user){
        try {
            log.info("trying to update password by id:{}",user.getIdUser());
            userDBService.saveUpdate(user);
        }catch (Exception e){
            log.error("error updating password of user id:{} error:{}",user.getIdUser(),e,e);
        }
    }

    public boolean getByEmailAndPassword(String email, String password) {
        try {
            log.info("trying to get by email and password");
            Optional<User> user= userDBService.getByEmailAndPassword(email,password);
            HttpSession session = httpSessionObjectFactory.getObject();
            if (user.isPresent()){
                log.info("user found going to landing..");
                session.setAttribute("user", user.get());
                return true;
            }else {

                log.info("user not found by email and password...");
//                model.addAttribute("noUserFound","No user found with given criteria");
//                httpServletRequest.setAttribute("errorMessage","No user found with given criteria");
                return false;
            }
        }catch (Exception e){
            log.error("error getting user by email and password{}",e,e);
            return false;
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
    public String getByEmailFb(User userFromFB) {
        try {

            log.info("trying to get by email");
            Optional<User> user = userDBService.getByEmail(userFromFB.getEmail());
            if (!user.isPresent()) {
                log.info("user not found tryint to insert");
                user = saveOrUpdate(userFromFB);
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

    public User getByID(long idUser) {
        try {
            log.info("trying to get  User by  id");
            Optional<User> byIDUser = userDBService.getById(idUser);
            if (byIDUser.isPresent()) {
                log.info("user has found by id");
               return byIDUser.get();
            } else {
                log.info("couldn't find by id{}", idUser);
               return null;
            }

        } catch (Exception e) {
            log.error("error get by id o user{}{}", e, e);
            return null;
        }

    }

    public boolean resetPassword(String token, HttpSession session) {
        Optional<User> byToken = userDBService.getByToken(token);
        return byToken
                .map(user -> {
                    session.setAttribute("user",user);
                    return true;
                })
                .orElseGet(()-> false);
    }

    public boolean confirm(String token, HttpSession session) {
        Optional<User> byToken = userDBService.getByToken(token);

        return byToken
                .map(user -> {
                    user.setIsConfirm(1);
                    userDBService.saveUpdate(user);
                    session.setAttribute("user",user);
                    return true;
                })
                .orElseGet(()-> false);
    }
}
