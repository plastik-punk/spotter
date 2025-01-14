package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface ApplicationUserService extends UserDetailsService {

    /**
     * Get the current Spring Security Authentication of the current user.
     *
     * @return a spring Security Authentication object of the current user
     */
    public Authentication getCurrentUserAuthentication();

    /**
     * Get a Application object of the user who is currently logged in.
     *
     * @return The ApplicationUser who is currently logged in.
     */
    public ApplicationUser getCurrentApplicationUser();

    /**
     * Find all staff accounts ((un)confirmed Admins and Employees) entries ordered by name (descending).
     *
     * @return ordered list of all staff account entries
     */
    List<ApplicationUser> findAll();

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Log in a user.
     *
     * @param applicationUserLoginDto login credentials
     * @return the JWT, if successful
     * @throws at.ac.tuwien.sepr.groupphase.backend.exception.BadCredentialsException if credentials are bad
     */
    String login(ApplicationUserLoginDto applicationUserLoginDto);


    //TODO: ADD validation Exception

    /**
     * Register a user.
     *
     * @param applicationUserRegistrationDto registration Data
     * @throws ConflictException if the user already exists
     */
    void register(ApplicationUserRegistrationDto applicationUserRegistrationDto) throws ConflictException;

    /**
     * Updates a user.
     *
     * @param toUpdate Data to update the user with
     * @throws NotFoundException if the user doesn't exist in the persistent data storage.
     */
    void update(ApplicationUserOverviewDto toUpdate) throws NotFoundException;

    /**
     * Delete the user with the given ID from the data storage.
     *
     * @param id ID of the user who should get deleted.
     * @throws NotFoundException if the user doesn't exist in the persistent data storage.
     * @throws ConflictException if the user has an reservation.
     */
    void delete(Long id) throws NotFoundException, ConflictException;


}
