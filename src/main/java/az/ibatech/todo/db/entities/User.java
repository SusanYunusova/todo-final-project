package az.ibatech.todo.db.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {
    /**
     * User-Shafa
     * long idUser
     * string fullName
     * string email
     * string password
     * string confirmPassword
     */
    private  static  final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser",nullable = false,unique = true)
    private  long idUser;
    private  String fullName;
    private  String email;
    private  String password;
    private String confirmPassword;
}
