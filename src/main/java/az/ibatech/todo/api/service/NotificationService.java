package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private  JavaMailSender javaMailSender;
    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public  void sendNotification(User user){
        //send mail
        SimpleMailMessage mail = new SimpleMailMessage();
    }
}
