package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

// TODO: validate via annotations

public class EventSearchDto {
    private LocalDate earliestDate;
    private LocalDate latestDate;
    private LocalTime earliestStartTime;
    private LocalTime latestEndTime;
    private String name;

    public LocalDate getEarliestDate() {
        return earliestDate;
    }

    public void setEarliestDate(LocalDate earliestDate) {
        this.earliestDate = earliestDate;
    }

    public LocalDate getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(LocalDate latestDate) {
        this.latestDate = latestDate;
    }

    public LocalTime getEarliestStartTime() {
        return earliestStartTime;
    }

    public void setEarliestStartTime(LocalTime earliestStartTime) {
        this.earliestStartTime = earliestStartTime;
    }

    public LocalTime getLatestEndTime() {
        return latestEndTime;
    }

    public void setLatestEndTime(LocalTime latestEndTime) {
        this.latestEndTime = latestEndTime;
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
        if (!(o instanceof EventSearchDto that)) {
            return false;
        }
        return Objects.equals(earliestDate, that.earliestDate)
            && Objects.equals(latestDate, that.latestDate)
            && Objects.equals(earliestStartTime, that.earliestStartTime)
            && Objects.equals(latestEndTime, that.latestEndTime)
            && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(earliestDate, latestDate, earliestStartTime, latestEndTime, name);
    }

    @Override
    public String toString() {
        return "EventSearchDto{"
            + "earliestDate=" + earliestDate
            + ", latestDate=" + latestDate
            + ", earliestStartTime=" + earliestStartTime
            + ", latestEndTime=" + latestEndTime
            + ", name=" + name
            + '}';
    }

    public static final class EventSearchDtoBuilder {
        private LocalDate earliestDate;
        private LocalDate latestDate;
        private LocalTime earliestStartTime;
        private LocalTime latestEndTime;
        private String name;

        private EventSearchDtoBuilder() {
        }

        public static EventSearchDtoBuilder anEventSearchDto() {
            return new EventSearchDtoBuilder();
        }

        public EventSearchDtoBuilder withEarliestDate(LocalDate earliestDate) {
            this.earliestDate = earliestDate;
            return this;
        }

        public EventSearchDtoBuilder withLatestDate(LocalDate latestDate) {
            this.latestDate = latestDate;
            return this;
        }

        public EventSearchDtoBuilder withEarliestStartTime(LocalTime earliestStartTime) {
            this.earliestStartTime = earliestStartTime;
            return this;
        }

        public EventSearchDtoBuilder withLatestEndTime(LocalTime latestEndTime) {
            this.latestEndTime = latestEndTime;
            return this;
        }

        public EventSearchDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventSearchDto build() {
            EventSearchDto eventSearchDto = new EventSearchDto();
            eventSearchDto.setEarliestDate(earliestDate);
            eventSearchDto.setLatestDate(latestDate);
            eventSearchDto.setEarliestStartTime(earliestStartTime);
            eventSearchDto.setLatestEndTime(latestEndTime);
            eventSearchDto.setName(name);
            return eventSearchDto;
        }
    }
}
