package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.Task;
import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.TaskDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Component
@Slf4j
public class TaskService {
    private final TaskDBService taskDBService;
    @Autowired
    private HttpServletRequest httpServletRequest;


    public TaskService(TaskDBService taskDBService) {
        this.taskDBService = taskDBService;
    }

    public void saveOrUpdate(Task task, HttpSession session,Model model) {
        try {
            Integer currentPage= (Integer) session.getAttribute("current");
            int page = currentPage==null?0:currentPage;

            Integer size = (Integer)session.getAttribute("pageSize");
            int count = size==null?5:size;
            log.info("trying to save or update Task");
            Optional<Task> savedTask = taskDBService.saveUpdate(task);
            Integer status = (Integer) session.getAttribute("taskStatus");
            if (status == null) {
                status = 0;
            }
            List<Task> tasksListByStatus = getByStatus(status, savedTask.get().getIdUser(),page,count);
//            if (tasksListByStatus.size()/count > 0) {
//            }
                log.info("page:{}",page);
                log.info("count:{}",count);
                log.info("list size:{}",tasksListByStatus.size());
                log.info("byStatus.size()/count:{}",tasksListByStatus.size()/count);

            model.addAttribute("currentPage",page);
            session.setAttribute("pageSize",count);
            User user = savedTask.get().getIdUser();
            int countByStatus = taskDBService.getTaskCountByStatus(user, status);
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i=1;i<=countByStatus/count;i++){
                pageNumbers.add(i);
            }
            if(countByStatus%count>0){
                pageNumbers.add(pageNumbers.size()+1);
            }
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, countByStatus%count==0?countByStatus/count+1:countByStatus/count+2)
//                        .boxed()
//                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                log.info("pageNumbers:{}",pageNumbers);
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

    private List<Task> getByStatus(int status, User idUser,int page,int count) {
        try {

            log.info("trying to get  taskList by  status");
            List<Task> taskList;
            if (status == 0) {
                taskList = taskDBService.getByIdUser(idUser,page,count);
            } else {
                taskList = taskDBService.getByStatus(status, idUser,page,count);
            }
            if (!taskList.isEmpty()) {
                log.info("taskList has found by status{}", status);
                return taskList;
            } else {
                log.info("couldn't find by status{}", status);
                return new ArrayList<>();
            }

        } catch (Exception e) {
            log.error("error get by idUser o taskList{}{}", e, e);
            return null;
        }
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

    public String getTaskByStatus(int status, HttpSession session, Model model) {
        log.info("trying to get tasks by status. status : {}", status);
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "index";
            }
            Integer currentPage= (Integer) session.getAttribute("current");
            int page = currentPage==null?0:currentPage;

            Integer size = (Integer)session.getAttribute("pageSize");
            int count = size==null?5:size;
            List<Task> byStatus = getByStatus(status, user,page,count);

//            if (byStatus.size()/count > 0) {
            int countByStatus = taskDBService.getTaskCountByStatus(user, status);
            log.info("count by status:{}",countByStatus);
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i=1;i<=countByStatus/count;i++){
                pageNumbers.add(i);
            }
            if(countByStatus%count>0){
                pageNumbers.add(pageNumbers.size()+1);
            }
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, countByStatus%count==0?countByStatus/count+1:countByStatus/count+2)                        .boxed()
//                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            log.info("pageNumbers:{}",pageNumbers);
//            }

            log.info("page:{}",page);
            log.info("count:{}",count);
            log.info("list size:{}",byStatus.size());
            log.info("byStatus.size()/count:{}",byStatus.size()/count);
            model.addAttribute("currentPage",page);
            session.setAttribute("pageSize",count);

            user.setTaskList(byStatus);
            session.setAttribute("user", user);
            session.setAttribute("status", status);
            return "tasks-dashboard";

        } catch (Exception e) {
            log.error("Error getting tasks by status : {}", e, e);
        }
        return "index";
    }

    public String getTaskArchive(int status, HttpSession session,Model model) {
        log.info("trying to get tasks archive. status : {}", status);
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "index";
            }
            Integer currentPage= (Integer) session.getAttribute("current");
            int page = currentPage==null?0:currentPage;

            Integer size = (Integer)session.getAttribute("pageSize");
            int count = size==null?5:size;
            List<Task> byStatus = getByStatus(status, user,page , count);
//            if (byStatus.size()/count > 0) {
            int countByStatus = taskDBService.getTaskCountByStatus(user, status);
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i=1;i<=countByStatus/count;i++){
                pageNumbers.add(i);
            }
            if(countByStatus%count>0){
                pageNumbers.add(pageNumbers.size()+1);
            }
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, countByStatus%count==0?countByStatus/count+1:countByStatus/count+2)
//                        .boxed()
//                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                log.info("pageNumbers:{}",pageNumbers);
//            }
                log.info("page:{}",page);
                log.info("count:{}",count);
                log.info("list size:{}",byStatus.size());
                log.info("byStatus.size()/count:{}",byStatus.size()/count);

            model.addAttribute("currentPage",page);
            session.setAttribute("pageSize",count);
            user.setTaskList(byStatus);
            session.setAttribute("user", user);
            session.setAttribute("status", status);
            return "tasks-archive";

        } catch (Exception e) {
            log.error("Error getting tasks by status : {}", e, e);
        }
        return "index";
    }

    public String getTaskByCurrentStatus(HttpSession session,Model model) {
        try {
            Integer currentPage= (Integer) session.getAttribute("current");
            int page = currentPage==null?0:currentPage;

            Integer size = (Integer)session.getAttribute("pageSize");
            int count = size==null?5:size;
            Integer status = (Integer) session.getAttribute("status");
            status=status==null?0:status;
            session.setAttribute("status",status);
            User user = (User) session.getAttribute("user");
            log.info("user:{}", user.getFullName());
            List<Task> byStatus = getByStatus(status, user,page, count);
//            if (byStatus.size()/count > 0) {
            int countByStatus = taskDBService.getTaskCountByStatus(user, status);
            log.info("count by status:{}",countByStatus);
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i=1;i<=countByStatus/count;i++){
                pageNumbers.add(i);
            }
            if(countByStatus%count>0){
                pageNumbers.add(pageNumbers.size()+1);
            }
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, countByStatus%count==0?countByStatus/count+1:countByStatus/count+2)                        .boxed()
//                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                log.info("pageNumbers:{}",pageNumbers);
//            }
                log.info("page:{}",page);
                log.info("count:{}",count);
                log.info("list size:{}",byStatus.size());
                log.info("byStatus.size()/count:{}",byStatus.size()/count);

            model.addAttribute("currentPage",page);
            session.setAttribute("pageSize",count);
            user.setTaskList(byStatus);
            session.setAttribute("user", user);
        } catch (Exception e) {
            log.error("error sendArchive...error:{}", e, e);
        }
        return "tasks-dashboard";
    }

//    public ResponseEntity<?> getArchive(long idUser) {
//        return null;
//    }
}
