package pl.maciek.app.activities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop3ByOrderByTermAsc();

    List<Activity> findAllByOrderByTermAsc();

    List<Activity> findAllByTitleContainingIgnoreCase(String title);

    @Query(value = "SELECT * FROM ACTIVITY WHERE TERM >= :startDate AND TERM <= :endDate", nativeQuery = true)
    List<Activity> findAllByTermBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT * FROM ACTIVITY WHERE TERM >= :startDate AND TERM <= :endDate AND TITLE LIKE %:title%"
            , nativeQuery = true)
    List<Activity> findAllByTitleAndTermBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("title") String title);

}
