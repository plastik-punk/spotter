package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Optional;


/**
 * Repository for the entity Place.
 * Extends the JpaRepository interface to provide basic CRUD operations.
 */
@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {


    /**
     * Finds the first free place for a reservation based on the given date, time, number of people, and status.
     * This method uses a native query to perform the search.
     *
     * @param date       the date of the reservation
     * @param startTime  the start time of the reservation
     * @param endTime    the end time of the reservation
     * @param pax        the number of people for the reservation
     * @param statusEnum the status of the place
     * @return an Optional containing a Place if a free place is found, otherwise an empty Optional
     */
    @Query(value = "SELECT TOP (1) p.* FROM place p "
        + "WHERE NOT EXISTS ("
        + " SELECT 1 FROM RESERVATION_PLACE rp"
        + " JOIN RESERVATION r on rp.RESERVATION_ID = r.ID"
        + " WHERE rp.PLACE_ID = p.ID"
        + " AND r.DATE = :date"
        + " AND ((r.start_time <= :startTime AND r.end_time > :startTime)"
        + " OR (r.start_time < :endTime AND r.end_time >= :endTime)"
        + " OR (r.start_time >= :startTime AND r.end_time <= :endTime))"
        + ") AND p.PAX >= :pax"
        + " AND p.STATUS = :statusEnum",
        nativeQuery = true)
    Optional<Place> findFreePlaceForReservation(
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime,
        @Param("pax") Long pax,
        @Param("statusEnum") StatusEnum statusEnum);


    /**
     * Finds the status of a place by its ID.
     *
     * @param placeId the ID of the place
     * @return the status of the place
     */
    @Query("SELECT p.status FROM Place p WHERE p.id = :placeId")
    StatusEnum findStatusById(@Param("placeId") Long placeId);
}