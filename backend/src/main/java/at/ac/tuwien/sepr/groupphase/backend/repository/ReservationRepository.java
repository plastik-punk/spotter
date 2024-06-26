package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository for the entity Reservation.
 * Extends JpaRepository to have basic CRUD operations.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Finds reservations that overlap with the specified time range on a given date.
     *
     * @param date      the date to check
     * @param startTime the start time to check
     * @param endTime   the end time to check
     * @return a list of reservation IDs that overlap with the specified time range
     */
    @Query("SELECT r.id "
        + "FROM Reservation r "
        + "WHERE r.date = :date "
        + "AND ((r.startTime < :startTime AND r.endTime > :startTime) "
        + "OR (r.startTime < :endTime AND r.endTime > :endTime) "
        + "OR (r.startTime >= :startTime AND r.endTime <= :endTime))")
    List<Long> findReservationsAtSpecifiedTime(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    /**
     * Finds reservations by user email and optional date and time ranges.
     *
     * @param email     the email of the user
     * @param startDate the optional start date
     * @param endDate   the optional end date
     * @param startTime the optional start time
     * @param endTime   the optional end time
     * @return a list of reservations that match the criteria
     */
    @Query(value = "SELECT r.* FROM reservation r "
        + "JOIN app_user u ON r.user_id = u.id "
        + "WHERE u.email = :email "
        + "AND (:startDate IS NULL OR r.date >= :startDate) "
        + "AND (:endDate IS NULL OR r.date <= :endDate) "
        + "AND (:startTime IS NULL OR r.start_time >= :startTime) "
        + "AND (:endTime IS NULL OR r.end_time <= :endTime)"
        + "AND (u.role != 3) ORDER BY r.date, r.start_time ASC", nativeQuery = true)
    List<Reservation> findReservationsByDate(
        @Param("email") String email,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime);

    /**
     * Finds reservations by their hash value.
     *
     * @param hashValue the hash value of the reservation
     * @return a list of reservations that match the hash value
     */
    List<Reservation> findByHashValue(String hashValue);

    /**
     * Finds all reservations on a specific date.
     *
     * @param date the date to check
     * @return a list of reservations on the specified date
     */
    List<Reservation> findAllByDate(LocalDate date);


    /**
     * Finds all reservations for a specific area on a specific date, excluding walk-in users.
     *
     * @param areaId the ID of the area
     * @param date   the date to check
     * @return a list of reservations for the specified area and date, excluding walk-in users
     */
    @Query("SELECT r FROM Reservation r "
        + "LEFT JOIN ReservationPlace rp ON r.id = rp.reservation.id "
        + "LEFT JOIN AreaPlaceSegment aps ON rp.place.id = aps.place.id "
        + "WHERE aps.area.id = :areaId "
        + "AND r.date = :date "
        + "AND r.applicationUser.id != 0")
    List<Reservation> findAllReservationsByAreaIdAndDateWithoutWalkInUsers(@Param("areaId") Long areaId, @Param("date") LocalDate date);


    /**
     * Finds all walk-in reservations for a specific area on a specific date.
     *
     * @param areaId the ID of the area
     * @param date   the date to check
     * @return a list of walk-in reservations for the specified area and date
     */
    @Query("SELECT r FROM Reservation r "
        + "LEFT JOIN ReservationPlace rp ON r.id = rp.reservation.id "
        + "LEFT JOIN AreaPlaceSegment aps ON rp.place.id = aps.place.id "
        + "WHERE aps.area.id = :areaId "
        + "AND r.date = :date "
        + "AND r.applicationUser.id = 0")
    List<Reservation> findAllWalkInReservationsByAreaIdAndDate(@Param("areaId") Long areaId, @Param("date") LocalDate date);


    /**
     * Finds reservations by the user ID.
     *
     * @param applicationUserId the ID of the user
     * @return a list of reservations for the specified user ID
     */
    @Query("SELECT r FROM Reservation r WHERE r.applicationUser.id = :applicationUserId")
    List<Reservation> findByApplicationUserId(@Param("applicationUserId") Long applicationUserId);


    /**
     * Finds reservations by optional date and time ranges, excluding reservations associated with a user ID.
     *
     * @param startDate the optional start date
     * @param endDate   the optional end date
     * @param startTime the optional start time
     * @param endTime   the optional end time
     * @return a list of reservations that match the criteria
     */
    @Query("SELECT r FROM Reservation r "
        + "WHERE (:startDate IS NULL OR r.date >= :startDate) "
        + "AND (:endDate IS NULL OR r.date <= :endDate) "
        + "AND (:startTime IS NULL OR r.startTime >= :startTime) "
        + "AND (:endTime IS NULL OR r.endTime <= :endTime) "
        + "ORDER BY r.date, r.startTime ASC")
    List<Reservation> findReservationsWithoutUserId(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime);

    /**
     * Finds all reservations on a specific date.
     *
     * @param localDate the date to check
     * @return a list of reservations on the specified date
     */
    List<Reservation> findAllReservationsByDate(LocalDate localDate);
}