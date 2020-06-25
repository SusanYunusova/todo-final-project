package az.ibatech.todo.db.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task implements Serializable {
    /**
     * Task-Fidan
     */
    private  static  final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTask",nullable = false,unique = true)
    private long idTask;
    @Column(name = "createdTime")
    private Date createdTime;
    @Column(name = "deadline")
    private Date deadline;
    @Column(name = "taskName")
    private String taskName;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private int status=0;

    @JoinColumn(name = "id_User", referencedColumnName = "id")
    @ManyToOne
    private User idUser;



}
