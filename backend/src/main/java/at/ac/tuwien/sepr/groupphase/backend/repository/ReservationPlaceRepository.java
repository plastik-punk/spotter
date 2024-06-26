package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Repository for the entity ReservationPlace.
 * Extends the JpaRepository interface to provide basic CRUD operations.
 */
@Repository
public interface ReservationPlaceRepository extends JpaRepository<ReservationPlace, Long> {

    /**
     * Finds a list of place IDs by a list of reservation IDs.
     *
     * @param reservationIds the list of reservation IDs
     * @return a list of distinct place IDs associated with the given reservation IDs
     */
    @Query("SELECT DISTINCT rp.place.id FROM ReservationPlace rp WHERE rp.reservation.id IN ?1")
    List<Long> findPlaceIdsByReservationIds(List<Long> reservationIds);


    /**
     * Deletes all ReservationPlace entities associated with a given reservation ID.
     *
     * @param reservationId the ID of the reservation
     */
    @Transactional
    @Modifying
    void deleteByReservationId(Long reservationId);

    /**
     * Finds a list of ReservationPlace entities by the reservation ID.
     *
     * @param reservationId the ID of the reservation
     * @return a list of ReservationPlace entities associated with the given reservation ID
     */
    List<ReservationPlace> findByReservationId(Long reservationId);

    /**
     * Finds a list of ReservationPlace entities by the place ID.
     *
     * @param placeId the ID of the place
     * @return a list of ReservationPlace entities associated with the given place ID
     */

    List<ReservationPlace> findByPlaceId(Long placeId);
}
