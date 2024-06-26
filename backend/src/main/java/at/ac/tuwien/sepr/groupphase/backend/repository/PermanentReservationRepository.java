package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


/**
 * Repository for the entity PermanentReservation.
 * Extends the JpaRepository interface to provide basic CRUD operations.
 */
@Repository
public interface PermanentReservationRepository extends JpaRepository<PermanentReservation, Long> {

    /**
     * Finds a PermanentReservation by its hashed ID.
     *
     * @param hashedId the hashed ID of the permanent reservation
     * @return the PermanentReservation entity
     */
    PermanentReservation findByHashedId(String hashedId);

    /**
     * Finds permanent reservations without considering user ID, based on given date and time ranges.
     *
     * @param startDate the start date filter (nullable)
     * @param endDate   the end date filter (nullable)
     * @param startTime the start time filter (nullable)
     * @param endTime   the end time filter (nullable)
     * @return a list of PermanentReservation entities
     */
    @Query("SELECT pr FROM PermanentReservation pr "
        + "WHERE (:startDate IS NULL OR pr.startDate >= :startDate) "
        + "AND (:endDate IS NULL OR pr.startDate <= :endDate) "
        + "AND (:startTime IS NULL OR pr.startTime >= :startTime) "
        + "AND (:endTime IS NULL OR pr.endTime <= :endTime) "
        + "ORDER BY pr.startDate, pr.startTime ASC")
    List<PermanentReservation> findPermanentReservationsWithoutUserId(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime);

    /**
     * Finds permanent reservations by user ID, based on given date and time ranges.
     *
     * @param userId    the user ID filter (nullable)
     * @param startDate the start date filter (nullable)
     * @param endDate   the end date filter (nullable)
     * @param startTime the start time filter (nullable)
     * @param endTime   the end time filter (nullable)
     * @return a list of PermanentReservation entities
     */
    @Query("SELECT pr FROM PermanentReservation pr "
        + "WHERE (:userId IS NULL OR pr.applicationUser.id = :userId) "
        + "AND (:startDate IS NULL OR pr.startDate >= :startDate) "
        + "AND (:endDate IS NULL OR pr.endDate <= :endDate) "
        + "AND (:startTime IS NULL OR pr.startTime >= :startTime) "
        + "AND (:endTime IS NULL OR pr.endTime <= :endTime) "
        + "ORDER BY pr.startDate, pr.startTime ASC")
    List<PermanentReservation> findPermanentReservationsByUserId(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime);

}

