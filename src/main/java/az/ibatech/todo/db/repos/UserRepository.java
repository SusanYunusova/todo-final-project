package az.ibatech.todo.db.repos;

import az.ibatech.todo.db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByIdUser(long idUser);

}
