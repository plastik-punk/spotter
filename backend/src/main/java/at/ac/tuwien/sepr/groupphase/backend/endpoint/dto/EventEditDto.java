package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.validation.EndTimeAfterStartTimeValidation;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@EndTimeAfterStartTimeValidation
public class EventEditDto {
    private String hashId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Description is required")
    private String description;

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

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
        if (!(o instanceof EventEditDto that)) {
            return false;
        }
        return hashId.equals(that.hashId)
            && startTime.equals(that.startTime)
            && endTime.equals(that.endTime)
            && name.equals(that.name)
            && description.equals(that.description);
    }

    @Override
    public String toString() {
        return "EventEditDto{"
            + "hashId='" + hashId + '\''
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", name='" + name + '\''
            + ", description='" + description + '\''
            + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashId, startTime, endTime, name, description);
    }

    public EventEditDto copy() {
        return EventEditDtoBuilder.anEventEditDto()
            .hashId(hashId)
            .startTime(startTime)
            .endTime(endTime)
            .name(name)
            .description(description)
            .build();
    }

    public static final class EventEditDtoBuilder {
        private String hashId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String name;
        private String description;

        private EventEditDtoBuilder() {
        }

        public static EventEditDtoBuilder anEventEditDto() {
            return new EventEditDtoBuilder();
        }

        public EventEditDtoBuilder hashId(String hashId) {
            this.hashId = hashId;
            return this;
        }

        public EventEditDtoBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventEditDtoBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventEditDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventEditDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventEditDto build() {
            EventEditDto eventEditDto = new EventEditDto();
            eventEditDto.setHashId(hashId);
            eventEditDto.setStartTime(startTime);
            eventEditDto.setEndTime(endTime);
            eventEditDto.setName(name);
            eventEditDto.setDescription(description);
            return eventEditDto;
        }
    }

}
