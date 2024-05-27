package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private String name;

    @Column(length = 100000)
    private String description;

    @Column(nullable = false)
    private String hashValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
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

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
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
            && start.equals(event.start)
            && end.equals(event.end)
            && name.equals(event.name)
            && description.equals(event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, name, description);
    }

    @Override
    public String toString() {
        return "Event{"
            + "id=" + id
            + ", start=" + start
            + ", end=" + end
            + ", name='" + name + '\''
            + ", description='" + description + '\''
            + '}';
    }

    public static final class EventBuilder {
        private Long id;
        private LocalDateTime start;
        private LocalDateTime end;
        private String name;
        private String description;
        private String hashValue;

        private EventBuilder() {
        }

        public static EventBuilder anEvent() {
            return new EventBuilder();
        }

        public EventBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder withStart(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public EventBuilder withEnd(LocalDateTime end) {
            this.end = end;
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

        public EventBuilder withHashValue(String hashValue) {
            this.hashValue = hashValue;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setStart(start);
            event.setEnd(end);
            event.setName(name);
            event.setDescription(description);
            event.setHashValue(hashValue);
            return event;
        }
    }
}
