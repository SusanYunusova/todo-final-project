package az.ibatech.todo.social.controller;

import az.ibatech.todo.api.service.UserService;
import az.ibatech.todo.social.service.FaceBookService;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class SpringFacebookController {
    private final FaceBookService faceBookService;
    private final UserService userService;
    public SpringFacebookController(FaceBookService faceBookService, UserService userService) {
        this.faceBookService = faceBookService;
        this.userService = userService;
    }

    @GetMapping(value = "/facebookLogin")
    public RedirectView facebookLogin() {
        RedirectView redirectView = new RedirectView();
        String url = faceBookService.facebookLogin();
        System.out.println("url:"+url);
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping(value = "/facebook")
    public String facebook(@RequestParam("code") String code) {
        String accesToken = faceBookService.getFacebookAccesToken(code);
        return "redirect:/facebookProfileData/" + accesToken;
    }

    @GetMapping(value = "facebookProfileData/{accessToken}")
    public String facebookProfileData(@PathVariable String accessToken, HttpSession session) {
        User user = faceBookService.getFacebookUserProfile(accessToken);
        az.ibatech.todo.db.entities.User sUser = az.ibatech.todo.db.entities.User.builder()
                .email(user.getEmail())
                .fullName(user.getFirstName().concat(user.getLastName()))
                .password(user.getId())
                .build();
        userService.getByEmailFb(sUser);
//        model.addAttribute("user",sUser);
        session.setAttribute("user",sUser);
        return "landing";
    }
}
