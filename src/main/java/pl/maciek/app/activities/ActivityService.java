package pl.maciek.app.activities;

import org.springframework.stereotype.Service;
import pl.maciek.app.user.User;
import pl.maciek.app.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private ActivityRepository activityRepository;
    private UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public List<Activity> find3nextActivities() {
        return activityRepository.findTop3ByOrderByTermAsc();
    }

    public List<Activity> findAllActivities() {
        return activityRepository.findAllByOrderByTermAsc();
    }

    public void saveActivity(Activity activity) {
        activityRepository.save(activity);
    }

    public void addUserToActivity(Activity activity, Long userId) {
        Optional<Activity> optionalActivity = activityRepository.findById(activity.getId());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalActivity.isPresent() && optionalUser.isPresent()) {
            Activity activityToUpdate = optionalActivity.get();
            User userToAdd = optionalUser.get();
            activityToUpdate.getParticipants().add(userToAdd);
            activityRepository.save(activityToUpdate);
        } else {
            System.err.println("Błąd podczas dodawania użytkownika do aktywności");
        }
    }

    public Activity findActivityById(Long id) {
        return activityRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public void updateActivity(Activity activity) {
        Optional<Activity> optionalActivity = activityRepository.findById(activity.getId());
        if (optionalActivity.isPresent()) {
            Activity activityToUpdate;
            activityToUpdate = optionalActivity.get();
            activityToUpdate.setTitle(activity.getTitle());
            activityToUpdate.setHallName(activity.getHallName());
            activityToUpdate.setTerm(activity.getTerm());
            activityRepository.save(activityToUpdate);
        } else {
            System.err.println("Błąd - brak aktywności w bazie");
        }
    }

    public List<Activity> findActivityByTitle(String searchingTitle) {
        return activityRepository.findAllByTitleContainingIgnoreCase(searchingTitle);
    }

    public List<Activity> findActivityByTermBeetween(LocalDate date) {
        LocalDateTime timeStart = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        LocalDateTime timeEnd = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        return activityRepository.findAllByTermBetween(timeStart, timeEnd);
    }

    public List<Activity> findActivityByTitleAndTerm(String title, LocalDate date) {
        LocalDateTime timeStart = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        LocalDateTime timeEnd = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        return activityRepository.findAllByTitleAndTermBetween(timeStart, timeEnd, title);
    }

    public List<Activity> getActivities(String searchingTitle, LocalDate searchingDate) {
        List<Activity> activities;
        if (searchingDate == null && searchingTitle == null) {
            activities = findAllActivities();
        } else if (searchingDate != null && searchingTitle != null) {
            activities = findActivityByTitleAndTerm(searchingTitle, searchingDate);
        } else if (searchingDate == null && searchingTitle != null) {
            activities = findActivityByTitle(searchingTitle);
        } else {
            activities = findActivityByTermBeetween(searchingDate);
        }
        return activities;
    }

}
