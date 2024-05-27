package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository for the entity Event.
 * Extends the JpaRepository interface to have basic CRUD operations.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e.* FROM event e "
        + "WHERE (:startDate IS NULL OR e.date >= :startDate) "
        + "AND (:endDate IS NULL OR e.date <= :endDate) "
        + "AND (:startTime IS NULL OR e.start_time >= :startTime) "
        + "AND (:endTime IS NULL OR e.end_time <= :endTime)"
        , nativeQuery = true)
    List<Event> findEventsByDate(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime);

    Event findByHashId(String hashId);
}
