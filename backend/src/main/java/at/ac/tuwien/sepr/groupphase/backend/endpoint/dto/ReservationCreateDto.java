package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalTime;
import java.time.LocalDate;


public class ReservationCreateDto {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private Long pax;
    private String notes;
    private String email;
    private int mobileNumber;
}
