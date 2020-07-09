package az.ibatech.todo.api.service;

import az.ibatech.todo.db.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
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
    public  void sendNotification(User user) throws MailException {
        //send mail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom("yunusova96.ys@gmail.com");
        mail.setSubject("my email test subject");
        mail.setText("Hello Suzy");
        javaMailSender.send(mail);
    }
}
