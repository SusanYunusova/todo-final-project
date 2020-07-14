package az.ibatech.todo.social.service.impl;

import az.ibatech.todo.social.service.FaceBookService;
import az.ibatech.todo.utility.AppConfiguration;
import com.sun.crypto.provider.OAEPParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
public class FacebookServiceImpl implements FaceBookService {
    @Value("${spring.social.facebook.app.id}")
    private String facebookId;
    @Value("${spring.social.facebook.app.secret}")
    private String facebookSecret;

    private final AppConfiguration appConfiguration;

    private  String uri;
    private  String scope="public_profile,email";

    @Autowired
    public FacebookServiceImpl(AppConfiguration appConfiguration) {
        uri="http://"+appConfiguration.getHost()+":"+appConfiguration.getPort()+"/facebook";
        this.appConfiguration = appConfiguration;
    }

    private FacebookConnectionFactory createFacebookConnection(){
        return new FacebookConnectionFactory(facebookId,facebookSecret);
    }

    @Override
    public String facebookLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(uri);
        parameters.setScope("public_profile,email");
        return createFacebookConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    @Override
    public String getFacebookAccesToken(String code) {

        return createFacebookConnection()
                .getOAuthOperations()
                .exchangeForAccess(code,uri,null)
                .getAccessToken();
    }

    @Override
    public User getFacebookUserProfile(String accesToken) {
        Facebook facebook = new FacebookTemplate(accesToken);
        String[] fields = {"id","first_name","last_name","email"};
        return facebook.fetchObject("me",User.class,fields);

    }
}
