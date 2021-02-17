package pl.maciek.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByRole(String role);

    List<UserRole> findByUser(User user);

    @Transactional
    List<UserRole> deleteByUser(User user);

}
