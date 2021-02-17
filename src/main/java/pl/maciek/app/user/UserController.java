package pl.maciek.app.user;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.maciek.app.activities.Activity;
import pl.maciek.app.activities.ActivityService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private ActivityService activityService;

    public UserController(UserService userService, ActivityService activityService) {
        this.userService = userService;
        this.activityService = activityService;
    }

    @GetMapping("/activities")
    public String showActivitiesList(Model model,
                                     @RequestParam(required = false) String searchingTitle,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchingDate) {
        List<Activity> activities = activityService.getActivities(searchingTitle, searchingDate);
        model.addAttribute("searchingTitle", searchingTitle);
        model.addAttribute("searchingDate", searchingDate);
        model.addAttribute("activities", activities);
        return "user/activitiesList";
    }

    @GetMapping("/activities/{id}/join")
    public String joinToActivity(Model model, @PathVariable Long id) {
        userService.joinToActivity(id);
        return "redirect:/activities";
    }

    @GetMapping("/edit-user")
    public String editUser(Model model, Principal principal) {
        User userToEdit = userService.findByName(principal.getName());
        model.addAttribute("user", userToEdit);
        return "user/userEdit";
    }

    @PostMapping("/edit-user")
    public String editAndSaveUser(@RequestParam Long id, @RequestParam String firstName, @RequestParam String lastName) {
        userService.updateUser(id, firstName, lastName);
        return "redirect:/";
    }

    @GetMapping("/edit-password")
    public String editUsersPassword(Model model, Principal principal) {
        User userToEdit = userService.findByName(principal.getName());
        model.addAttribute("user", userToEdit);
        return "user/passwordEdit";
    }

    @PostMapping("/edit-password")
    public String editAndSaveNewPassword(User user, @RequestParam String password, Model model) {
        userService.updatePassword(user, password);
        return "redirect:/edit-user";
    }

}
