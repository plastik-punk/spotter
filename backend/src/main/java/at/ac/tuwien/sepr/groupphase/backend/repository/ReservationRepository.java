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
    @Query(value = "SELECT r.* FROM reservation r WHERE "
        + "(:startDate IS NULL OR r.date >= :startDate) "
        + "AND (:endDate IS NULL OR r.date <= :endDate) "
        + "AND (:startTime IS NULL OR r.start_time >= :startTime) "
        + "AND (:endTime IS NULL OR r.end_time <= :endTime)", nativeQuery = true)
    List<Reservation> findReservationsByDate(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime);
}
