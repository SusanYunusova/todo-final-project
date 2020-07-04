package az.ibatech.todo.db.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {
    /**
     * User-Shafa
     * long id
     * string fullName
     * string email
     * string password
     * string confirmPassword
     */
    private  static  final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private  long idUser;
    @Column(name = "fullName")
    private  String fullName;
    @Column(name = "email")
    private  String email;
    @Column(name = "password")
    private  String password;
//    @Column(name = "confirmPassword")
//    private String confirmPassword;

    @OneToMany(mappedBy = "idUser")
    @JsonManagedReference
    private List<Task> taskList;
}
