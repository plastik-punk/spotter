package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservationMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PermanentReservationMapperRepository extends JpaRepository<PermanentReservationMapper, Long> {

    List<PermanentReservationMapper> findByPermanentReservationId(Long permanentReservationId);

    @Transactional
    @Modifying
    void deleteByReservationId(Long reservationId);
}
