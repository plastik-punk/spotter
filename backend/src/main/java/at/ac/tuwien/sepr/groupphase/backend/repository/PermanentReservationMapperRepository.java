package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservationMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Repository for the entity PermanentReservationMapper.
 * Extends the JpaRepository interface to provide basic CRUD operations.
 */
@Repository
public interface PermanentReservationMapperRepository extends JpaRepository<PermanentReservationMapper, Long> {


    /**
     * Finds all PermanentReservationMapper entities by the permanent reservation ID.
     *
     * @param permanentReservationId the ID of the permanent reservation
     * @return a list of PermanentReservationMapper entities
     */
    List<PermanentReservationMapper> findByPermanentReservationId(Long permanentReservationId);


    /**
     * Deletes PermanentReservationMapper entities by the reservation ID.
     * This method is transactional and uses modifying queries.
     *
     * @param reservationId the ID of the reservation to delete mappings for
     */
    @Transactional
    @Modifying
    void deleteByReservationId(Long reservationId);
}
