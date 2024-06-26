package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for the entity Segment.
 * Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
}