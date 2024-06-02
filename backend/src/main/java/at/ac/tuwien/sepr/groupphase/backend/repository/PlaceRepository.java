package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findFirstByPaxAndStatusOrderByPaxDesc(Long pax, StatusEnum status);

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

}
