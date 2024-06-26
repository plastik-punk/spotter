package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Area} entity.
 * Provides methods to perform CRUD operations and custom queries.
 */
@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    /**
     * Finds the first {@link Area} entity ordered by ID in ascending order.
     *
     * @return an {@link Optional} containing the first {@link Area} entity if found, or empty if not
     */
    Optional<Area> findFirstByOrderByIdAsc();
}
