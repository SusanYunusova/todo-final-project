package az.ibatech.todo.db.service.impl;

import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.repos.UserRepository;
import az.ibatech.todo.db.service.MySqlDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserDBService implements MySqlDBService<User> {
    private final UserRepository userRepository;

    public UserDBService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> saveUpdate(User user) {
        try {
            log.info("trying to save user to db...");
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            log.error("error saving user to db...{}", e, e);
            return Optional.empty();
        }
    }

    public Optional<User> getByEmail(String email){
        try {
            log.info("trying to get user by email");
           return userRepository.findByEmail(email);
        }catch (Exception e){
            log.error("erro gettin email {}",e,e);
            return Optional.empty();
        }
    }
    @Override
    public boolean delete(User user) {
        try {
            log.info("trying to delete user from db");
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            log.error("error from deleting user from db{}", e, e);
            return false;
        }
    }

    @Override
    public Optional<User> getById(long idUser) {
        try {
            log.info("trying to get user by id from db");
            return userRepository.findByIdUser(idUser);
        } catch (Exception e) {
            log.error("error getting by idUser from Db{}{}", e, e);
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        try {
            log.info("trying to getAll users from db");
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("error getting all from db{}{}", e, e);
            return new ArrayList<>();
        }

    }

    @Override
    public List<User> getAllBy(long id) {

        return null;
    }
//    public void getByPassword(User user){
//        Optional<User> byEmailAndPassword = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
//
//
//    }
}
