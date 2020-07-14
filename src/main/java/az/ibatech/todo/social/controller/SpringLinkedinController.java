package az.ibatech.todo.social.controller;

import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.social.service.LinkedinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.facebook.api.User;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
@Slf4j
@Controller
public class SpringLinkedinController {
    private final LinkedinService linkedinService;
    private final UserService userService;

    public SpringLinkedinController(LinkedinService linkedinService, UserService userService) {
        this.linkedinService = linkedinService;
        this.userService = userService;
    }

    @GetMapping(value = "/linkedinLogin")
    public RedirectView linkedinLogin() {
        log.info("linkedinLogin..................");
        RedirectView redirectView = new RedirectView();
        String url = linkedinService.linkedinLogin();
        System.out.println("url:"+url);
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping(value = "/linkedin")
    public String facebook(@RequestParam("code") String code) {
        log.info("linkedin..................");

        String accessToken = linkedinService.getLinkedinAccesToken(code);
        return "redirect:/linkedinProfileData/" + accessToken;
    }

    @GetMapping(value = "linkedinProfileData/{accessToken}")
    public String facebookProfileData(@PathVariable String accessToken, HttpSession session) {
        log.info("linkedinProfileData..................");

        LinkedInProfileFull user = linkedinService.getLinkedinUserProfile(accessToken);
        log.info("profilefull usermail:{}",user.getEmailAddress());
        az.ibatech.todo.db.entities.User sUser = az.ibatech.todo.db.entities.User.builder()
                .email(user.getEmailAddress())
                .fullName(user.getFirstName().concat(user.getLastName()))
                .password(user.getId())
                .build();
        log.info("userFromLinkedin:{}",sUser.getFullName());
        userService.getByEmailFb(sUser);
//        model.addAttribute("user",sUser);
        session.setAttribute("user",sUser);
        return "landing";
    }
}
