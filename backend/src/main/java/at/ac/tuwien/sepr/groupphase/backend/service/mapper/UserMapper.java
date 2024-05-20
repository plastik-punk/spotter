package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    ApplicationUser userRegistrationDtoToApplicationUser(UserRegistrationDto userRegistrationDto);

    @Mapping(target = "password", ignore = true)
    UserRegistrationDto applicationUserToUserRegistrationDto(ApplicationUser applicationUser);

    @Named("userOverview")
    UserOverviewDto applicationUserToUserOverviewDto(ApplicationUser applicationUser);

    @IterableMapping(qualifiedByName = "userOverview")
    List<UserOverviewDto> applicationUserToUserOverviewDto(List<ApplicationUser> applicationUser);
}
