package pl.maciek.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.maciek.app.activities.Activity;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    List<User> findAllByActivitiesNotContains(Activity activity);

    List<User> findAllByIdIsNotLike(Long id);
}
