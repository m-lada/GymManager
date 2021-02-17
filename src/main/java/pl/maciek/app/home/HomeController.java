package pl.maciek.app.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.maciek.app.activities.Activity;
import pl.maciek.app.activities.ActivityService;

import java.util.List;

@Controller
public class HomeController {

    private ActivityService activityService;

    public HomeController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Activity> activities = activityService.find3nextActivities();
        model.addAttribute("activities", activities);
        return "home";
    }
}
