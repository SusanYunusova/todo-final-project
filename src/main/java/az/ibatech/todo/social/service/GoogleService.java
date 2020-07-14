package az.ibatech.todo.social.service;

import org.springframework.social.facebook.api.User;
import org.springframework.social.google.api.plus.Person;

public interface GoogleService {
    String googleLogin();

    String getGoogleAccesToken(String code);

    Person getGoogleUserProfile(String accesToken);
}
