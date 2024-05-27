package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventDetailDto {
    private String hashId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
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
        if (!(o instanceof EventDetailDto that)) {
            return false;
        }
        return hashId.equals(that.hashId)
            && startTime.equals(that.startTime)
            && endTime.equals(that.endTime)
            && name.equals(that.name)
            && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashId, startTime, endTime, name, description);
    }

    @Override
    public String toString() {
        return "EventDetailDto{" +
            "hashId=" + hashId +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

    public EventDetailDto copy() {
        return EventDetailDtoBuilder.anEventDetailDto()
            .hashId(hashId)
            .startTime(startTime)
            .endTime(endTime)
            .name(name)
            .description(description)
            .build();
    }

    public static final class EventDetailDtoBuilder {
        private String hashId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String name;
        private String description;

        private EventDetailDtoBuilder() {
        }

        private static EventDetailDtoBuilder anEventDetailDto() {
            return new EventDetailDtoBuilder();
        }

        public EventDetailDtoBuilder hashId(String hashId) {
            this.hashId = hashId;
            return this;
        }

        public EventDetailDtoBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventDetailDtoBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventDetailDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventDetailDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventDetailDto build() {
            EventDetailDto eventDetailDto = new EventDetailDto();
            eventDetailDto.setHashId(hashId);
            eventDetailDto.setStartTime(startTime);
            eventDetailDto.setEndTime(endTime);
            eventDetailDto.setName(name);
            eventDetailDto.setDescription(description);
            return eventDetailDto;
        }
    }

}
