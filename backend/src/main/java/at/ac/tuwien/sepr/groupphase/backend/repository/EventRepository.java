package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the entity Event.
 * Extends the JpaRepository interface to have basic CRUD operations.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
}
