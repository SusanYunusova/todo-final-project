package az.ibatech.todo.social.service;

import org.springframework.social.linkedin.api.LinkedInProfileFull;

public interface LinkedinService {
    String linkedinLogin();

    String getLinkedinAccesToken(String code);

    LinkedInProfileFull getLinkedinUserProfile(String accessToken);
}
