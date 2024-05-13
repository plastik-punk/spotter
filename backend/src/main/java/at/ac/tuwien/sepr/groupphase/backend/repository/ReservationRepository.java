package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the entity Reservation.
 * Extends JpaRepository to have basic CRUD operations.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}