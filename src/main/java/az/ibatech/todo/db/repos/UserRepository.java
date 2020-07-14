package az.ibatech.todo.db.repos;

import az.ibatech.todo.db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByIdUser(long idUser);
    Optional<User> findByEmailAndPasswordAndIsConfirm(String email,String password,int isConfirm);
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);

}
