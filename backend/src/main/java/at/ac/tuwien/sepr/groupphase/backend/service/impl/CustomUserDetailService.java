package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
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
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final UserDataValidator userDataValidator;
    private final UserMapper userMapper;

    @Autowired
    public CustomUserDetailService(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder,
                                   JwtTokenizer jwtTokenizer, UserDataValidator userDataValidator, UserMapper userMapper) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.userDataValidator = userDataValidator;
        this.userMapper = userMapper;
    }


    public Authentication getCurrentUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }
        return null;
    }

    @Override
    public ApplicationUser getCurrentUser() {
        Authentication currentAuthentication = getCurrentUserAuthentication();
        if (currentAuthentication == null) {
            return null;
        } else {
            ApplicationUser existingUser = applicationUserRepository.findByEmail(currentAuthentication.getName());
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
    public Authentication getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }
        return null; // or throw an appropriate exception if the user is not authenticated.
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getRole() == RoleEnum.ADMIN) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
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
        ApplicationUser applicationUser = applicationUserRepository.findByEmail(email);
        System.out.println(email);
        System.out.println(applicationUser);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public String login(UserLoginDto userLoginDto) throws BadCredentialsException {
        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())
        ) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }
        throw new BadCredentialsException("Username or password is incorrect or account is locked");
    }

    @Override
    public void register(UserRegistrationDto userRegistrationDto) throws ValidationException {
        LOGGER.trace("register ({})", userRegistrationDto);
        userDataValidator.validateRegistration(userRegistrationDto);
        ApplicationUser applicationUser = userMapper.userRegistrationDtoToApplicationUser(userRegistrationDto);
        applicationUser.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        applicationUserRepository.save(applicationUser);
    }

    @Override
    public void update(UserOverviewDto toUpdate) throws NotFoundException {
        LOGGER.trace("update ({})", toUpdate);
        ApplicationUser existingUser = applicationUserRepository.findById(toUpdate.getId())
            .orElseThrow(() -> new NotFoundException("User not found with id: " + toUpdate.getId()));

        existingUser.setFirstName(toUpdate.getFirstName());
        existingUser.setLastName(toUpdate.getLastName());
        existingUser.setEmail(toUpdate.getEmail());
        existingUser.setMobileNumber(toUpdate.getMobileNumber());
        existingUser.setRole(toUpdate.getRole());

        applicationUserRepository.save(existingUser);
    }
}
