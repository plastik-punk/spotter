package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.validation.EndDateAndTimeAfterStartValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

@EndDateAndTimeAfterStartValidation
public class EventCreateDto {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalTime startTime;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private LocalTime endTime;

    @NotNull(message = "Name is required")
    @Size(min = 1, message = "Name should not be empty")
    @Size(max = 255, message = "Name shouldn't be longer than 255 characters")
    private String name;

    @Size(max = 100000, message = "Description shouldn't be longer than 100000 characters")
    private String description;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCreateDto that)) {
            return false;
        }
        return startDate.equals(that.startDate)
            && startTime.equals(that.startTime)
            && endDate.equals(that.endDate)
            && endTime.equals(that.endTime)
            && name.equals(that.name)
            && description.equals(that.description);
    }

    @Override
    public String toString() {
        return "EventCreateDto{"
            + "startDate=" + startDate
            + ", startTime=" + startTime
            + ", endDate=" + endDate
            + ", endTime=" + endTime
            + ", name='" + name + '\''
            + ", description='" + description + '\''
            + '}';
    }

    public EventCreateDto copy() {
        return EventCreateDtoBuilder.anEventCreateDto()
            .withStartDate(startDate)
            .withStartTime(startTime)
            .withEndDate(endDate)
            .withEndTime(endTime)
            .withName(name)
            .withDescription(description)
            .build();
    }

    public static final class EventCreateDtoBuilder {
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private String name;
        private String description;

        private EventCreateDtoBuilder() {
        }

        public static EventCreateDtoBuilder anEventCreateDto() {
            return new EventCreateDtoBuilder();
        }

        public EventCreateDtoBuilder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public EventCreateDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventCreateDtoBuilder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public EventCreateDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventCreateDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventCreateDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EventCreateDto build() {
            EventCreateDto eventCreateDto = new EventCreateDto();
            eventCreateDto.setStartDate(startDate);
            eventCreateDto.setStartTime(startTime);
            eventCreateDto.setEndDate(endDate);
            eventCreateDto.setEndTime(endTime);
            eventCreateDto.setName(name);
            eventCreateDto.setDescription(description);
            return eventCreateDto;
        }
    }
}
