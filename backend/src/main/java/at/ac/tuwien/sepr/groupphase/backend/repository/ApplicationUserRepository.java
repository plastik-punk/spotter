package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository for application users.
 * Extends JpaRepository to get basic CRUD operations.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    /**
     * Find an application user based on the email address and role.
     *
     * @param email the email address
     * @param role  the role we exclude
     * @return an application user
     */
    ApplicationUser findByEmailAndRoleNot(String email, RoleEnum role);

    /**
     * Find all users by role.
     *
     * @param roles user with which roles are to be fetched
     * @return sorted list by first name of users who have the given roles
     */
    List<ApplicationUser> findByRoleInOrderByFirstNameAsc(List<RoleEnum> roles);

    List<ApplicationUser> findAllByRole(RoleEnum role);

    /**
     * Insert a user with a specific id. This is used to insert users with custom ids.
     *
     * @param id           the id of the user
     * @param firstName    the first name of the user
     * @param lastName     the last name of the user
     * @param email        the email of the user
     * @param mobileNumber the mobile number of the user
     * @param password     the password of the user
     * @param role         the role of the user
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO app_user (id, first_name, last_name, email, mobile_number, password, role) "
        + "VALUES (:id, :firstName, :lastName, :email, :mobileNumber, :password, :role)", nativeQuery = true)
    void insertUserWithId(@Param("id") Long id,
                          @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("email") String email,
                          @Param("mobileNumber") String mobileNumber,
                          @Param("password") String password,
                          @Param("role") RoleEnum role);
}