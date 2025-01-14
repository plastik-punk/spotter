package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.BadCredentialsException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ApplicationUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final ApplicationUserMapper applicationUserMapper;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder,
                                      JwtTokenizer jwtTokenizer, ApplicationUserMapper applicationUserMapper,
                                      ReservationRepository reservationRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.applicationUserMapper = applicationUserMapper;
        this.reservationRepository = reservationRepository;
    }


    public Authentication getCurrentUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }
        return null;
    }

    @Override
    public ApplicationUser getCurrentApplicationUser() {
        Authentication currentAuthentication = getCurrentUserAuthentication();
        if (currentAuthentication == null) {
            return null;
        } else {
            ApplicationUser existingUser = applicationUserRepository.findByEmailAndRoleNot(currentAuthentication.getName(), RoleEnum.GUEST);
            return existingUser;
        }
    }

    @Override
    public List<ApplicationUser> findAll() {
        LOGGER.trace("Find all staff accounts");
        String currentUserEmail = getCurrentUserAuthentication().getName();
        List<RoleEnum> roles = Arrays.asList(RoleEnum.ADMIN, RoleEnum.UNCONFIRMED_ADMIN, RoleEnum.EMPLOYEE, RoleEnum.UNCONFIRMED_EMPLOYEE);
        List<ApplicationUser> userList = applicationUserRepository.findByRoleInOrderByFirstNameAsc(roles);
        List<ApplicationUser> filteredUserList = userList.stream()
            .filter(user -> !user.getEmail().equals(currentUserEmail))
            .collect(Collectors.toList());
        LOGGER.debug("Filtered User List ({})", filteredUserList);
        return filteredUserList;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getRole() == RoleEnum.ADMIN) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_USER");
            } else if (applicationUser.getRole() == RoleEnum.EMPLOYEE) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_EMPLOYEE", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = applicationUserRepository.findByEmailAndRoleNot(email, RoleEnum.GUEST);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException("Could not find the user with this email address");
    }

    @Override
    public String login(ApplicationUserLoginDto applicationUserLoginDto) throws BadCredentialsException {
        UserDetails userDetails = loadUserByUsername(applicationUserLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && passwordEncoder.matches(applicationUserLoginDto.getPassword(), userDetails.getPassword())
        ) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            LOGGER.debug("[LOGIN] Security Roles: {}", roles);
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }
        throw new BadCredentialsException("Username or password is incorrect or account is locked");
    }

    @Override
    public void register(ApplicationUserRegistrationDto applicationUserRegistrationDto) throws ConflictException {
        LOGGER.trace("register ({})", applicationUserRegistrationDto);

        ApplicationUser existingUser = applicationUserRepository.findByEmailAndRoleNot(applicationUserRegistrationDto.getEmail(), RoleEnum.GUEST);
        LOGGER.debug("Existing User: {}", existingUser);
        if (existingUser != null) {
            throw new ConflictException("Account already exists",
                List.of("An account for this email address already exists"));
        }
        ApplicationUser applicationUser = applicationUserMapper.userRegistrationDtoToApplicationUser(applicationUserRegistrationDto);
        applicationUser.setPassword(passwordEncoder.encode(applicationUserRegistrationDto.getPassword()));
        applicationUserRepository.save(applicationUser);
    }

    @Override
    public void update(ApplicationUserOverviewDto toUpdate) throws NotFoundException {
        LOGGER.trace("update ({})", toUpdate);
        ApplicationUser existingUser = applicationUserRepository.findById(toUpdate.getId())
            .orElseThrow(() -> new NotFoundException("No such user found in database"));

        existingUser.setFirstName(toUpdate.getFirstName());
        existingUser.setLastName(toUpdate.getLastName());
        existingUser.setEmail(toUpdate.getEmail());
        existingUser.setMobileNumber(toUpdate.getMobileNumber());
        existingUser.setRole(toUpdate.getRole());

        applicationUserRepository.save(existingUser);
    }

    @Override
    public void delete(Long id) throws NotFoundException, ConflictException {
        LOGGER.trace("delete ({})", id);
        if (reservationRepository.findByApplicationUserId(id).size() != 0) {
            throw new ConflictException("Error upon deleting user",
                Arrays.asList("Couldn't delete this user because they have open reservations"));
        }

        if (!applicationUserRepository.existsById(id)) {
            throw new NotFoundException("No such user found in database");
        }
        applicationUserRepository.deleteById(id);
    }
}