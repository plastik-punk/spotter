package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventListDto {
    private String hashId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventListDto that)) {
            return false;
        }
        return hashId.equals(that.hashId)
            && startTime.equals(that.startTime)
            && endTime.equals(that.endTime)
            && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashId, startTime, endTime, name);
    }

    @Override
    public String toString() {
        return "EventListDto{" +
            "hashId=" + hashId +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", name='" + name + '\'' +
            '}';
    }

    public static final class EventListDtoBuilder {
        private String hashId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String name;

        private EventListDtoBuilder() {
        }

        public static EventListDtoBuilder anEventListDto() {
            return new EventListDtoBuilder();
        }

        public EventListDtoBuilder withHashId(String hashId) {
            this.hashId = hashId;
            return this;
        }

        public EventListDtoBuilder withStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventListDtoBuilder withEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventListDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventListDto build() {
            EventListDto eventListDto = new EventListDto();
            eventListDto.setHashId(hashId);
            eventListDto.setStartTime(startTime);
            eventListDto.setEndTime(endTime);
            eventListDto.setName(name);
            return eventListDto;
        }
    }
}
