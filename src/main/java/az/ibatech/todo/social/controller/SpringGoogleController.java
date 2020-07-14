package az.ibatech.todo.social.controller;

import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.social.service.GoogleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.facebook.api.User;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class SpringGoogleController {
    private final GoogleService googleService;
    private final UserService userService;
    public SpringGoogleController(GoogleService googleService, UserService userService) {
        this.googleService = googleService;
        this.userService = userService;
    }

    @GetMapping(value = "/googleLogin")
    public RedirectView googleLogin() {
        log.info("googleLogin............");
        RedirectView redirectView = new RedirectView();
        String url = googleService.googleLogin();
        System.out.println("url:"+url);
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping(value = "/google")
    public String google(@RequestParam("code") String code) {
        log.info("google............");
        String accesToken = googleService.getGoogleAccesToken(code);
        return "redirect:/googleProfileData/" + accesToken;
    }

    @GetMapping(value = "googleProfileData/{accessToken}")
    public String googleProfileData(@PathVariable String accessToken, HttpSession session) {
        log.info("googleProfileData............");
        Person user = googleService.getGoogleUserProfile(accessToken);
        log.info("user from google:{}",user.getAccountEmail());
        az.ibatech.todo.db.entities.User sUser = az.ibatech.todo.db.entities.User.builder()
                .email(user.getAccountEmail())
                .fullName(user.getGivenName().concat(user.getFamilyName()))
                .password(user.getId())
                .build();
        userService.getByEmailFb(sUser);
//        model.addAttribute("user",sUser);
        session.setAttribute("user",sUser);
        return "landing";
    }
}
