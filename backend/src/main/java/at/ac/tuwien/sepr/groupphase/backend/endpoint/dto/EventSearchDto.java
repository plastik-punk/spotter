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
    private Long maxResults;

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

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
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
            && Objects.equals(maxResults, that.maxResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(earliestDate, latestDate, earliestStartTime, latestEndTime, maxResults);
    }

    @Override
    public String toString() {
        return "EventSearchDto{"
            + "earliestDate=" + earliestDate
            + ", latestDate=" + latestDate
            + ", earliestStartTime=" + earliestStartTime
            + ", latestEndTime=" + latestEndTime
            + ", maxResults=" + maxResults
            + '}';
    }

    public static final class EventSearchDtoBuilder {
        private LocalDate earliestDate;
        private LocalDate latestDate;
        private LocalTime earliestStartTime;
        private LocalTime latestEndTime;
        private Long maxResults;

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

        public EventSearchDtoBuilder withMaxResults(Long maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public EventSearchDto build() {
            EventSearchDto eventSearchDto = new EventSearchDto();
            eventSearchDto.setEarliestDate(earliestDate);
            eventSearchDto.setLatestDate(latestDate);
            eventSearchDto.setEarliestStartTime(earliestStartTime);
            eventSearchDto.setLatestEndTime(latestEndTime);
            eventSearchDto.setMaxResults(maxResults);
            return eventSearchDto;
        }
    }
}
