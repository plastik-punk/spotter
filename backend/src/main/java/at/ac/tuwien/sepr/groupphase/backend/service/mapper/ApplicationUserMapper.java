package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


/**
 * Mapper interface for converting between {@link ApplicationUser} entities and their corresponding DTOs.
 * This interface uses MapStruct for generating the implementation.
 */
@Mapper(componentModel = "spring")
public interface ApplicationUserMapper {

    /**
     * Converts an {@link ApplicationUserRegistrationDto} to an {@link ApplicationUser}.
     * The {@code id} and {@code password} fields are ignored during mapping.
     *
     * @param applicationUserRegistrationDto the DTO to convert
     * @return the converted {@link ApplicationUser}
     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    ApplicationUser userRegistrationDtoToApplicationUser(ApplicationUserRegistrationDto applicationUserRegistrationDto);


    /**
     * Converts an {@link ApplicationUser} to an {@link ApplicationUserOverviewDto}.
     * This method is qualified by the {@code userOverview} name.
     *
     * @param applicationUser the entity to convert
     * @return the converted {@link ApplicationUserOverviewDto}
     */
    @Named("userOverview")
    ApplicationUserOverviewDto applicationUserToUserOverviewDto(ApplicationUser applicationUser);


    /**
     * Converts a list of {@link ApplicationUser} entities to a list of {@link ApplicationUserOverviewDto}.
     * This method uses the {@code userOverview} qualified method for individual conversions.
     *
     * @param applicationUser the list of entities to convert
     * @return the converted list of {@link ApplicationUserOverviewDto}
     */
    @IterableMapping(qualifiedByName = "userOverview")
    List<ApplicationUserOverviewDto> applicationUserToUserOverviewDto(List<ApplicationUser> applicationUser);
}
