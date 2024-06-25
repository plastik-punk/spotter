package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationUserMapper {

    @Mapping(target = "password", ignore = true)
    ApplicationUser userRegistrationDtoToApplicationUser(ApplicationUserRegistrationDto applicationUserRegistrationDto);

    @Mapping(target = "password", ignore = true)
    ApplicationUserRegistrationDto applicationUserToUserRegistrationDto(ApplicationUser applicationUser);

    @Named("userOverview")
    ApplicationUserOverviewDto applicationUserToUserOverviewDto(ApplicationUser applicationUser);

    @IterableMapping(qualifiedByName = "userOverview")
    List<ApplicationUserOverviewDto> applicationUserToUserOverviewDto(List<ApplicationUser> applicationUser);
}
