package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository for the entity ReservationOffer.
 * Extends the JpaRepository interface to provide basic CRUD operations.
 */
@Repository
public interface ReservationOfferRepository extends JpaRepository<ReservationOffer, Long> {

    /**
     * Finds a list of ReservationOffer entities by the reservation ID.
     *
     * @param id the ID of the reservation
     * @return a list of ReservationOffer entities associated with the given reservation ID
     */
    List<ReservationOffer> findByReservationId(Long id);
}
