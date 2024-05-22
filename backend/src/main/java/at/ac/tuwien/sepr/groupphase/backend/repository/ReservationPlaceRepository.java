package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReservationPlaceRepository extends JpaRepository<ReservationPlace, Long> {

    @Query("SELECT DISTINCT rp.place.id FROM ReservationPlace rp WHERE rp.reservation.id IN ?1")
    List<Long> findPlaceIdsByReservationIds(List<Long> reservationIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM ReservationPlace rp WHERE rp.reservation.id = ?1")
    void deleteByReservationId(Long reservationId);

    List<ReservationPlace> findByReservationId(Long reservationId);

}
