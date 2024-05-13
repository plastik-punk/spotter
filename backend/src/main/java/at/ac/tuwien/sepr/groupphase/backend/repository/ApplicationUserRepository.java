package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for application users.
 * Extends JpaRepository to get basic CRUD operations.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return an application user
     */
    ApplicationUser findByEmail(String email);
}