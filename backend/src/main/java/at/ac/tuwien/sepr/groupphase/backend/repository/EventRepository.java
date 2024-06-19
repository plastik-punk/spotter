package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for the entity Event.
 * Extends the JpaRepository interface to have basic CRUD operations.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e.* FROM event e "
        + "WHERE (:startTime IS NULL OR e.start_time >= :startTime) "
        + "AND (:endTime IS NULL OR e.start_time <= :endTime) ",
        nativeQuery = true)
    List<Event> findEventsByDate(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime);

    @Query(value = "SELECT e.* FROM event e "
        + "WHERE (:startTime IS NULL OR e.start_time >= :startTime) "
        + "AND (:endTime IS NULL OR e.start_time <= :endTime) "
        + "ORDER BY e.start_time ASC "
        + "LIMIT :maxResults",
        nativeQuery = true)
    List<Event> findEventsByDateMax(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("maxResults") Long maxResults);

    Event findByHashId(String hashId);
}
