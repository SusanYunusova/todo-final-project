package az.ibatech.todo.api.service;

import az.ibatech.todo.api.service.internal.MailDetails;
import az.ibatech.todo.db.entities.User;
import az.ibatech.todo.db.service.impl.UserDBService;
import az.ibatech.todo.utility.AppConfiguration;
import az.ibatech.todo.utility.GenerateCustomToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class NotificationService {
    private JavaMailSender javaMailSender;
    private final UserDBService userDBService;
    private final GenerateCustomToken generateCustomToken;
    private final AppConfiguration appConfiguration;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender, UserDBService userDBService, GenerateCustomToken generateCustomToken, AppConfiguration appConfiguration) {
        this.javaMailSender = javaMailSender;
        this.userDBService = userDBService;
        this.generateCustomToken = generateCustomToken;
        this.appConfiguration = appConfiguration;
    }

    public boolean sendNotificationReset(String email, HttpSession session) throws MailException {

        Optional<User> user = getUser(email);
        return user
                .map(this::sendMailForReset)
                .orElseGet(() -> false);

    }



    private boolean sendMailForReset(User user) {
        //send mail
        String token = generateCustomToken.generateToken();
        user.setToken(token);
        userDBService.saveUpdate(user);
        MailDetails mailDetails = MailDetails.builder()
                .subject("Todo app password reset")
                .text("Hello " + user.getFullName() + " to reset your password click link : http://" + appConfiguration.getHost() + ":" + appConfiguration.getPort() + "/api/new-password/" + token)
                .to(user.getEmail())
                .build();
        sendMail(mailDetails);
        return true;
    }

    private void sendMail(MailDetails mailDetails) {
        //send mail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailDetails.getTo());
        mail.setFrom(appConfiguration.getMail());
        mail.setSubject(mailDetails.getSubject());
        mail.setText(mailDetails.getText());
        javaMailSender.send(mail);
    }

    public boolean sendMailForConfirm(User user) {
        //send mail
        MailDetails mailDetails = MailDetails.builder()
                .to(user.getEmail())
                .text("Hello " + user.getFullName() + " to confirm your registration click link : http://" + appConfiguration.getHost() + ":" + appConfiguration.getPort() + "/api/confirm/" + user.getToken())
                .subject("TOdoApp-confirm user")
                .build();
        sendMail(mailDetails);
        return true;
    }

    private Optional<User> getUser(String email) {
        return userDBService.getByEmail(email);
    }
}
