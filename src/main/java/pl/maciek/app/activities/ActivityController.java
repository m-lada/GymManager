package pl.maciek.app.activities;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.maciek.app.user.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class ActivityController {

    private UserDtoMapper userDtoMapper;
    private ActivityService activityService;
    private UserService userService;

    public ActivityController(UserDtoMapper userDtoMapper, ActivityService activityService, UserService userService) {
        this.userDtoMapper = userDtoMapper;
        this.activityService = activityService;
        this.userService = userService;
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public String argumentError() {
        return "activityError";
    }

    @GetMapping("/activities")
    public String showActivitiesList(Model model,
                                     @RequestParam(required = false) String searchingTitle,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchingDate) {
        List<Activity> activities = activityService.getActivities(searchingTitle, searchingDate);
        model.addAttribute("searchingTitle", searchingTitle);
        model.addAttribute("searchingDate", searchingDate);
        model.addAttribute("activities", activities);
        return "admin/activitiesList";
    }

    @GetMapping("/activities/{id}/users")
    public String showUsersForChosenAcitivity(@PathVariable Long id, Model model) {
        Activity chosenActivity = activityService.findActivityById(id);
        List<User> usersReadyToAdd = userService.getUsersWithoutCurrentUser();
        model.addAttribute("usersReadyToAdd", usersReadyToAdd);
        model.addAttribute("activity", chosenActivity);
        model.addAttribute("users", chosenActivity.getParticipants());
        return "admin/activityUsers";
    }

    @GetMapping("/activities/{id}/edit")
    public String editChosenActivity(@PathVariable Long id, Model model) {
        Activity chosenActivity = activityService.findActivityById(id);
        model.addAttribute("activity", chosenActivity);
        model.addAttribute("mode", "edit");
        return "admin/activityAddOrEdit";
    }

    @GetMapping("/activities/add")
    public String addActivity(Model model) {
        model.addAttribute("activity", new Activity());
        model.addAttribute("mode", "add");
        return "admin/activityAddOrEdit";
    }

    @GetMapping("/activities/{activityId}/delete-user/{userId}")
    public String deleteUserFromActivity(@PathVariable Long activityId, @PathVariable Long userId, Model model) {
        userService.removeActivityForUser(userId, activityId);
        Activity chosenActivity = activityService.findActivityById(activityId);
        model.addAttribute("users", chosenActivity.getParticipants());
        return "redirect:/admin/activities/" + activityId + "/users";
    }

    @GetMapping("/activities/{activityId}/add-user")
    public String listOfUsersToAddToActivity(@PathVariable Long activityId, Model model) {
        Activity chosenActivity = activityService.findActivityById(activityId);
        List<User> usersWithoutChosenActivity = userService.findUsersWithoutChosenActivity(chosenActivity);
        model.addAttribute("users", usersWithoutChosenActivity);
        model.addAttribute("activity", chosenActivity);
        return "admin/usersToAdd";
    }

    @GetMapping("/activities/{activityId}/add-user/{userId}")
    public String addUserToActivity(@PathVariable Long activityId, @PathVariable Long userId, Model model) {
        Activity chosenActivity = activityService.findActivityById(activityId);
        userService.addUserToActivity(chosenActivity, userId);
        List<User> usersWithoutChosenActivity = userService.findUsersWithoutChosenActivity(chosenActivity);
        model.addAttribute("users", usersWithoutChosenActivity);
        model.addAttribute("activity", chosenActivity);
        return "redirect:/admin/activities/" + activityId + "/add-user";
    }

    @PostMapping("/activities/add")
    public String addActivityAndSave(Activity addedActivity, Model model) {
        List<Activity> activities = activityService.findAllActivities();
        activityService.saveActivity(addedActivity);
        model.addAttribute("activities", activities);
        return "redirect:/admin/activities";
    }

    @PostMapping("/activities/edit")
    public String editActivityAndSave(Activity addedActivity, Model model) {
        activityService.updateActivity(addedActivity);
        List<Activity> activities = activityService.findAllActivities();
        model.addAttribute("activities", activities);
        return "redirect:/admin/activities";
    }

    @GetMapping("/users")
    public String getUserList(Model model) {
        List<User> users = userService.getUsersWithoutCurrentUser();
        model.addAttribute("users", users);
        return "admin/usersList";
    }

    @GetMapping("/users/{id}/edit")
    public String getUser(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserDtoFromUser(id);
        model.addAttribute("user", userDto);
        return "admin/userEdit";
    }

    @PostMapping("/user/edit")
    public String editUser(UserDto userDto) {
        userDtoMapper.saveDtoChangesForUser(userDto);
        return "redirect:/admin/users";
    }
}
