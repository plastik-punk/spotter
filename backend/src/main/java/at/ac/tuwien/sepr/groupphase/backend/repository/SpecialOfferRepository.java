package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the entity SpecialOffer.
 * Extends JpaRepository to provide basic CRUD operations.
 */

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, Long> {

}
