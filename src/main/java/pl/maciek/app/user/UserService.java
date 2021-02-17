package pl.maciek.app.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.maciek.app.activities.Activity;
import pl.maciek.app.activities.ActivityRepository;
import pl.maciek.app.security.UserAuthService;

import java.util.*;

@Service
public class UserService {

    private UserDtoMapper userDtoMapper;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private ActivityRepository activityRepository;
    private UserAuthService userAuthService;

    public UserService(UserDtoMapper userDtoMapper, UserRepository userRepository, UserRoleRepository userRoleRepository, ActivityRepository activityRepository, UserAuthService userAuthService) {
        this.userDtoMapper = userDtoMapper;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.activityRepository = activityRepository;
        this.userAuthService = userAuthService;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findUsersWithoutChosenActivity(Activity chosenActivity) {
        return userRepository.findAllByActivitiesNotContains(chosenActivity);
    }

    public void addUserToActivity(Activity activity, Long userId) {
        Optional<Activity> optionalActivity = activityRepository.findById(activity.getId());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalActivity.isPresent() && optionalUser.isPresent()) {
            Activity activityToAdd = optionalActivity.get();
            User userToUpdate = optionalUser.get();

            userToUpdate.addActivity(activityToAdd);
            userRepository.save(userToUpdate);

        } else {
            System.err.println("Błąd podczas dodawania użytkownika do aktywności");
        }
    }

    public void removeActivityForUser(Long userId, Long activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);
        Optional<User> foundedUser = userRepository.findById(userId);

        if (activity.isPresent() && foundedUser.isPresent()) {
            User replacedUser = foundedUser.get();
            replacedUser.getActivities().remove(activity.get());
            userRepository.save(replacedUser);
        } else {
            System.err.println("Błąd zapisu użytkownika");
        }
    }

    public List<User> getUsersWithoutCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User currentUser = userRepository.findByUserName(userName);
        List<User> allByIdNotContaining = userRepository.findAllByIdIsNotLike(currentUser.getId());
        return allByIdNotContaining;
    }

    public User getUsersById(Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public User findByName(String name) {
        return userRepository.findByUserName(name);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public void updatePassword(User user, String password) {
        Optional<User> foundedUser = userRepository.findById(user.getId());
        if (foundedUser.isPresent()) {
            User userToUpdate = foundedUser.get();
            userAuthService.updatePassword(userToUpdate, password);
            userRepository.save(userToUpdate);
        }
    }

    public void updateUser(Long id, String firstName, String lastName) {
        User userToEdit = findById(id);
        userToEdit.setFirstName(firstName);
        userToEdit.setLastName(lastName);
        userRepository.save(userToEdit);
    }

    public UserDto getUserDtoFromUser(Long id) {
        User userToEdit = getUsersById(id);
        UserDto userDto = userDtoMapper.userToDto(userToEdit);
        return userDto;
    }

    public void joinToActivity(Long activityId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User currentUser = userRepository.findByUserName(userName);

        Optional<Activity> optionalActivity = activityRepository.findById(activityId);
        if (optionalActivity.isPresent()) {
            Activity activityToAdd = optionalActivity.get();
            List<Activity> activities = currentUser.getActivities();
            boolean activityWasChoosen = activities.stream()
                    .anyMatch(activity -> activity.getId() == activityToAdd.getId());
            if (!activityWasChoosen) {
                currentUser.addActivity(activityToAdd);
                userRepository.save(currentUser);
            }
        } else {
            System.err.println("Błąd zapisu użytkownika");
        }
    }
}
