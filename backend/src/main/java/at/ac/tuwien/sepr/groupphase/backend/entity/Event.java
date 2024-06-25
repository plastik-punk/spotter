package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.validation.EndTimeAfterStartTimeValidation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EndTimeAfterStartTimeValidation
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Column(nullable = false)
    private LocalDateTime endTime;

    @NotNull(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Column(length = 100000)
    private String description;

    @Column(nullable = false)
    private String hashId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashValue) {
        this.hashId = hashValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event event)) {
            return false;
        }
        return Objects.equals(id, event.id)
            && startTime.equals(event.startTime)
            && endTime.equals(event.endTime)
            && name.equals(event.name)
            && description.equals(event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, name, description);
    }

    @Override
    public String toString() {
        return "Event{"
            + "id=" + id
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", name='" + name + '\''
            + ", description='" + description + '\''
            + '}';
    }

    public static final class EventBuilder {
        private Long id;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String name;
        private String description;
        private String hashId;

        private EventBuilder() {
        }

        public static EventBuilder anEvent() {
            return new EventBuilder();
        }

        public EventBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder withStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public EventBuilder withEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public EventBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder withHashId(String hashId) {
            this.hashId = hashId;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            event.setName(name);
            event.setDescription(description);
            event.setHashId(hashId);
            return event;
        }
    }
}
