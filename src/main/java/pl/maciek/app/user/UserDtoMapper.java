package pl.maciek.app.user;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static pl.maciek.app.user.Role.ROLE_ADMIN;
import static pl.maciek.app.user.Role.ROLE_USER;

@Component
public class UserDtoMapper {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    public UserDtoMapper(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public UserDto userToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                userIsAdmin(user)
        );
    }

    public void saveDtoChangesForUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            setRoleForUser(user, userDto.isAdmin());
        }
    }

    @Transactional
    public void setRoleForUser(User user, boolean isAdmin) {
        userRoleRepository.deleteByUser(user);
        if (isAdmin) {
            UserRole userRole = new UserRole(user, ROLE_USER);
            UserRole adminRole = new UserRole(user, ROLE_ADMIN);
            userRoleRepository.save(userRole);
            userRoleRepository.save(adminRole);
        } else {
            UserRole userRole = new UserRole(user, ROLE_USER);
            userRoleRepository.save(userRole);
        }
    }

    public boolean userIsAdmin(User user) {
        Set<UserRole> roles = user.getRoles();
        return roles.stream()
                .anyMatch(userRole -> userRole.getRole().name() == ROLE_ADMIN.name());
    }

}
