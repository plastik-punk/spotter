package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;

public class EventCreateDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
    private String description;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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
        return startTime.equals(that.startTime)
            && endTime.equals(that.endTime)
            && name.equals(that.name)
            && description.equals(that.description);
    }

    @Override
    public String toString() {
        return "EventCreateDto{" +
            "startTime=" + startTime +
            ", endTime=" + endTime +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

    public EventCreateDto copy() {
        return EventCreateDtoBuilder.anEventCreateDto()
            .withStartTime(startTime)
            .withEndTime(endTime)
            .withName(name)
            .withDescription(description)
            .build();
    }

    public static final class EventCreateDtoBuilder {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String name;
        private String description;

        private EventCreateDtoBuilder() {
        }

        public static EventCreateDtoBuilder anEventCreateDto() {
            return new EventCreateDtoBuilder();
        }

        public EventCreateDtoBuilder withStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventCreateDtoBuilder withEndTime(LocalDateTime endTime) {
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
            eventCreateDto.setStartTime(startTime);
            eventCreateDto.setEndTime(endTime);
            eventCreateDto.setName(name);
            eventCreateDto.setDescription(description);
            return eventCreateDto;
        }
    }
}
