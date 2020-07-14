package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.TaskDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
@Slf4j
public class TaskService {
    private final TaskDBService taskDBService;
    @Autowired
    private HttpServletRequest httpServletRequest;


    public TaskService(TaskDBService taskDBService) {
        this.taskDBService = taskDBService;
    }

    public void saveOrUpdate(Task task, HttpSession session) {
        try {
            log.info("trying to save or update Task");
            Optional<Task> savedTask = taskDBService.saveUpdate(task);
            Integer status = (Integer) session.getAttribute("taskStatus");
            if (status == null) {
                status = 0;
            }
            List<Task> tasksListByStatus = getByStatus(status, savedTask.get().getIdUser());
            User user = savedTask.get().getIdUser();
            user.setTaskList(tasksListByStatus);
            session.setAttribute("user", user);
        } catch (Exception e) {
            log.error("error save or update task{}{}", e, e);
        }
    }


    public boolean delete(long id) {
        try {
            log.info("trying to delete  Task by  id");
            Optional<Task> byIDTask = taskDBService.getById(id);
            if (byIDTask.isPresent()) {
                taskDBService.delete(byIDTask.get());
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("error save or update task{}{}", e, e);

            return false;
        }
    }

    public ResponseEntity<Optional<Task>> getByID(long id) {
        try {
            log.info("trying to get  task by  id");
            Optional<Task> byIDTask = taskDBService.getById(id);
            if (byIDTask.isPresent()) {
                log.info("task has found by id");
                return new ResponseEntity<>(byIDTask, HttpStatus.OK);
            } else {
                log.info("couldn't find by id{}", id);
                return new ResponseEntity<>(Optional.empty(), HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error("error get by id o task{}{}", e, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//    public List<Task> getByIDUser(long idUser) {
//        try {
//            log.info("trying to get  taskList by  idUser");
//            List<Task> taskList = taskDBService.getByIdUser(idUser);
////            httpServletRequest.setAttribute("taskList", taskList);
//            return taskList;
//        } catch (Exception e) {
//            log.error("error get by idUser o taskList{}{}", e, e);
//            return new ArrayList<>();
//        }
//    }

    private List<Task> getByStatus(int status, User idUser) {
        try {
            log.info("trying to get  taskList by  status");
            List<Task> taskList;
            if (status == 0) {
                taskList = taskDBService.getByIdUser(idUser);
            } else {
                taskList = taskDBService.getByStatus(status, idUser);
            }
            if (!taskList.isEmpty()) {
                log.info("taskList has found by status{}", status);
                return taskList;
            } else {
                log.info("couldn't find by status{}", status);
            }

        } catch (Exception e) {
            log.error("error get by idUser o taskList{}{}", e, e);

        }
        return null;
    }

    public void complete(long idTask) {
        try {
            log.info("trying to complete  task by  idTask");
            Optional<Task> updatedTask = taskDBService.complete(idTask);

            if (updatedTask.isPresent()) {
                log.info("task has updated complete by idTask{}", idTask);
            } else {
                log.info("couldn't update by idTask{}", idTask);
            }

        } catch (Exception e) {
            log.error("error update complete task{}{}", e, e);
        }
    }

    public void sendArchive(long idTask) {
        try {
            log.info("trying to sendArchive  task by  idTask");
            Optional<Task> updatedTask = taskDBService.sendArchive(idTask);
            if (updatedTask.isPresent()) {
                log.info("task has updated sendArchive by idTask{}", idTask);
            } else {
                log.info("couldn't update by idTask{}", idTask);
            }

        } catch (Exception e) {
            log.error("error update sendArchive task{}{}", e, e);
        }
    }

    public String getTaskByStatus(int status, HttpSession session) {
        log.info("trying to get tasks by status. status : {}", status);
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "index";
            }

            List<Task> byStatus = getByStatus(status, user);
            user.setTaskList(byStatus);
            session.setAttribute("user", user);
            session.setAttribute("status", status);
            return "tasks-dashboard";

        } catch (Exception e) {
            log.error("Error getting tasks by status : {}", e, e);
        }
        return "index";
    }

    public String getTaskArchive(int status, HttpSession session) {
        log.info("trying to get tasks archive. status : {}", status);
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "index";
            }

            List<Task> byStatus = getByStatus(status, user);
            user.setTaskList(byStatus);
            session.setAttribute("user", user);
            session.setAttribute("status", status);
            return "tasks-archive";

        } catch (Exception e) {
            log.error("Error getting tasks by status : {}", e, e);
        }
        return "index";
    }

    public String getTaskByCurrentStatus(HttpSession session) {
        Integer status = (Integer) session.getAttribute("status");
        User user = (User) session.getAttribute("user");
        List<Task> byStatus = getByStatus(status, user);
        user.setTaskList(byStatus);
        session.setAttribute("user", user);
        return "tasks-dashboard";
    }

//    public ResponseEntity<?> getArchive(long idUser) {
//        return null;
//    }
}
