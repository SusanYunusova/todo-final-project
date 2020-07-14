package az.ibatech.todo.social.service.impl;

import az.ibatech.todo.social.service.FaceBookService;
import az.ibatech.todo.social.service.GoogleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
public class GoogleServiceImpl implements GoogleService {
    @Value("${spring.social.google.app.id}")
    private String googleId;
    @Value("${spring.social.google.app.secret}")
    private String googleSecret;

    private  String uri="http://localhost:9606/google";
    private  String scope="public_profile,email";

    private GoogleConnectionFactory createGoogleConnection(){
        return new GoogleConnectionFactory(googleId,googleSecret);
    }

    @Override
    public String googleLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(uri);
        parameters.setScope("profile");
        return createGoogleConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    @Override
    public String getGoogleAccesToken(String code) {

        return createGoogleConnection()
                .getOAuthOperations()
                .exchangeForAccess(code,uri,null)
                .getAccessToken();
    }

    @Override
    public Person getGoogleUserProfile(String accesToken) {
        Google google = new GoogleTemplate(accesToken);
       return google.plusOperations().getGoogleProfile();

    }
}
