package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermanentReservationRepository extends JpaRepository<PermanentReservation, Long> {

    List<PermanentReservation> findByHashedId(String hashValue);
}
