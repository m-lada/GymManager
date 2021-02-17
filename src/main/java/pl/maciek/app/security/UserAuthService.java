package pl.maciek.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.maciek.app.user.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class UserAuthService {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(UserRoleRepository roleRepository) {
        this.userRoleRepository = roleRepository;
    }

    public void addWithDefaultRole(User user) {
        Role defaultRole = Role.ROLE_USER;
        UserRole userRole = new UserRole(user, defaultRole);

        List<UserRole> list = Collections.singletonList(userRole);
        user.setRoles(new HashSet<>(list));

        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);

        userRepository.save(user);
        userRoleRepository.save(userRole);
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }
}
