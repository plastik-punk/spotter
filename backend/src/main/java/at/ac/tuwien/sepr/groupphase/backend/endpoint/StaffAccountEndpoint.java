package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ApplicationUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/employees")
public class StaffAccountEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationUserService applicationUserService;
    private final ApplicationUserMapper applicationUserMapper;

    public StaffAccountEndpoint(ApplicationUserService applicationUserService, ApplicationUserMapper applicationUserMapper) {
        this.applicationUserService = applicationUserService;
        this.applicationUserMapper = applicationUserMapper;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationUserOverviewDto> findAll() {
        LOGGER.info("GET /api/v1/employees");
        return applicationUserMapper.applicationUserToUserOverviewDto(applicationUserService.findAll());
    }


    @PutMapping("{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> update(@PathVariable("id") long id, @RequestBody ApplicationUserOverviewDto toUpdate)
        throws NotFoundException {
        LOGGER.info("PUT /api/v1/employees " + "/{}", toUpdate);
        applicationUserService.update(toUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws NotFoundException, ConflictException {
        LOGGER.info("DELETE /api/v1/employees/{}", id);
        try {
            applicationUserService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

