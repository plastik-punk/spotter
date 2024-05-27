package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EventService;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final EventMapper mapper;
    private final HashService hashService;
    private final EventValidator eventValidator;
    private final ApplicationUserServiceImpl applicationUserService;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            ApplicationUserRepository applicationUserRepository,
                            EventMapper mapper,
                            HashService hashService,
                            EventValidator eventValidator,
                            ApplicationUserServiceImpl applicationUserService) {
        this.eventRepository = eventRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.mapper = mapper;
        this.hashService = hashService;
        this.eventValidator = eventValidator;
        this.applicationUserService = applicationUserService;
    }
}
