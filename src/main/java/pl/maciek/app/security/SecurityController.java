package pl.maciek.app.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.maciek.app.user.User;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SecurityController {

    private UserAuthService userAuthService;

    public SecurityController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error,
                            Model model) {
        boolean showErrorMessage = false;
        if (error != null) {
            showErrorMessage = true;
        }

        model.addAttribute("showErrorMessage", showErrorMessage);
        return "security/login_form";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "security/register_form";
    }

    @PostMapping("/register")
    public String addUser(Model model,
                          @Valid @ModelAttribute User user,
                          BindingResult bindResult) {
        boolean showErrorMessage = false;

        if (bindResult.hasErrors()) {
            showErrorMessage = true;
            model.addAttribute("showErrorMessage", showErrorMessage);
            model.addAttribute("user", user);
            return "security/register_form";
        } else {
            userAuthService.addWithDefaultRole(user);
            return "security/login_form";
        }
    }
}
