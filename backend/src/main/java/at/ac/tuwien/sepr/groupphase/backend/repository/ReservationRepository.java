package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
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

    @Query("SELECT r.id "
        + "FROM Reservation r "
        + "WHERE r.date = :date "
        + "AND ((r.startTime < :startTime AND r.endTime > :startTime) "
        + "OR (r.startTime < :endTime AND r.endTime > :endTime) "
        + "OR (r.startTime >= :startTime AND r.endTime <= :endTime))")
    List<Long> findReservationsAtSpecifiedTime(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}