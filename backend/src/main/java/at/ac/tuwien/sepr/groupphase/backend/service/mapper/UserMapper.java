package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //Ignore password because its hashed and set individually
    @Mapping(target = "password", ignore = true)
    ApplicationUser userRegistrationDtoToApplicationUser(UserRegistrationDto userRegistrationDto);

    UserRegistrationDto applicationUserToUserRegistrationDto(ApplicationUser applicationUser);
}
