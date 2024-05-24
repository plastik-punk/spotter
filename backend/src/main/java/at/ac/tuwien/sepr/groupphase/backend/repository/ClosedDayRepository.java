package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ClosedDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ClosedDayRepository extends JpaRepository<ClosedDay, Long> {

    Optional<ClosedDay> findByDate(LocalDate date);
}
