package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for application users.
 * Extends JpaRepository to get basic CRUD operations.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    // TODO: this seems to expect to find exactly one user (or none) with the given email. Hence, validation on user creation needs to check if email is already in use for a registered user

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return an application user
     */
    ApplicationUser findByEmail(String email);

    /**
     * Find all users by role.
     *
     * @param roles user with which roles are to be fetched
     * @return sorted list by first name of users who have the given roles
     */
    List<ApplicationUser> findByRoleInOrderByFirstNameAsc(List<RoleEnum> roles);

    List<ApplicationUser> findAllByRole(RoleEnum role);
}