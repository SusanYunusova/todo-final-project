package az.ibatech.todo.social.service;

import org.springframework.social.facebook.api.User;

public interface FaceBookService {
    String facebookLogin();

    String getFacebookAccesToken(String code);

    User getFacebookUserProfile(String accesToken);
}
