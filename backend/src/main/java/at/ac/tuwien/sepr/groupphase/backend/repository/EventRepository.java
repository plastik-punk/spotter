package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository for the entity Event.
 * Extends the JpaRepository interface to have basic CRUD operations.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Finds events based on the provided date and time range and name.
     *
     * @param startDate the start date to filter events (nullable)
     * @param endDate   the end date to filter events (nullable)
     * @param startTime the start time to filter events (nullable)
     * @param endTime   the end time to filter events (nullable)
     * @param name      the name to filter events by (nullable)
     * @return a list of events matching the criteria
     */
    @Query(value = "SELECT e.* FROM event e "
        + "WHERE (:startDate IS NULL OR CAST(e.start_time AS DATE) >= :startDate) "
        + "AND (:endDate IS NULL OR CAST(e.start_time AS DATE) <= :endDate) "
        + "AND (:startTime IS NULL OR CAST(e.start_time AS TIME) >= :startTime) "
        + "AND (:endTime IS NULL OR CAST(e.end_time AS TIME) <= :endTime) "
        + "AND (:name IS NULL OR LOWER(e.name) LIKE LOWER('%' || :name || '%')) "
        + "ORDER BY e.start_time ASC",
        nativeQuery = true)
    List<Event> findEventsByDate(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime,
        @Param("name") String name);

    /**
     * Finds upcoming events within the next year.
     *
     * @return a list of upcoming events
     */
    @Query(value = "SELECT e.* FROM event e "
        + "WHERE e.end_time >= CURRENT_TIMESTAMP "
        + "AND e.start_time <= DATEADD('YEAR', 1, CURRENT_TIMESTAMP) "
        + "ORDER BY e.start_time ASC",
        nativeQuery = true)
    List<Event> findUpcomingEvents();

    /**
     * Finds an event by its hash ID.
     *
     * @param hashId the hash ID of the event
     * @return the event with the specified hash ID
     */
    Event findByHashId(String hashId);

    /**
     * Finds all events between the specified start and end times.
     *
     * @param startTime the start time to filter events
     * @param endTime   the end time to filter events
     * @return a list of events within the specified time range
     */
    List<Event> findAllByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Finds all events before the specified start time and after the specified end time.
     *
     * @param startTime the start time to filter events
     * @param endTime   the end time to filter events
     * @return a list of events within the specified time range
     */
    List<Event> findAllByStartTimeBeforeAndStartTimeAfter(LocalDateTime startTime, LocalDateTime endTime);

}
