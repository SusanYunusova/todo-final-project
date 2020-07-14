package az.ibatech.todo.social.service.impl;

import az.ibatech.todo.social.service.LinkedinService;
import az.ibatech.todo.utility.AppConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
public class LinkedinServiceImpl implements LinkedinService {
    @Value("${spring.social.linkedin.app.id}")
    private String linkedinId;
    @Value("${spring.social.linkedin.app.secret}")
    private String linkedinSecret;

    private final AppConfiguration appConfiguration;
    private  String uri;
    private  String scope="public_profile,email";

    public LinkedinServiceImpl(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
        uri="http://"+appConfiguration.getHost()+":"+appConfiguration.getPort()+"/linkedin";
    }

    private LinkedInConnectionFactory createLinkedinConnection(){
        return new LinkedInConnectionFactory(linkedinId,linkedinSecret);
    }
    @Override
    public String linkedinLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(uri);
        parameters.setScope("r_emailaddress,r_liteprofile");
        return createLinkedinConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    @Override
    public String getLinkedinAccesToken(String code) {
        return createLinkedinConnection()
                .getOAuthOperations()
                .exchangeForAccess(code,uri,null)
                .getAccessToken();
    }

    @Override
    public LinkedInProfileFull getLinkedinUserProfile(String accessToken) {
        LinkedIn linkedIn = new LinkedInTemplate(accessToken);
        return linkedIn.profileOperations().getUserProfileFull();
    }
}
