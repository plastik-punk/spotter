package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private ApplicationUser applicationUser;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private LocalTime endTime;

    @Column(nullable = false)
    private Long pax;

    @Column(length = 100000)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false, referencedColumnName = "id")
    private Place place;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setUser(ApplicationUser user) {
        this.applicationUser = user;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Long getPax() {
        return pax;
    }

    public void setPax(Long pax) {
        this.pax = pax;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation reservation)) {
            return false;
        }
        return Objects.equals(id, reservation.id)
            && Objects.equals(applicationUser, reservation.applicationUser)
            && Objects.equals(startTime, reservation.startTime)
            && Objects.equals(date, reservation.date)
            && Objects.equals(endTime, reservation.endTime)
            && Objects.equals(pax, reservation.pax)
            && Objects.equals(notes, reservation.notes)
            && Objects.equals(place, reservation.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, applicationUser, startTime, date, endTime, pax, notes, place);
    }

    @Override
    public String toString() {
        return "Reservation{"
            + "id=" + id
            + ", applicationUser=" + applicationUser
            + ", startTime=" + startTime
            + ", date=" + date
            + ", endTime=" + endTime
            + ", pax=" + pax
            + ", notes='" + notes + '\''
            + ", place=" + place
            + '}';
    }

    public Reservation copy() {
        return ReservationBuilder.aReservation()
            .withId(this.id)
            .withUser(this.applicationUser)
            .withStartTime(this.startTime)
            .withDate(this.date)
            .withEndTime(this.endTime)
            .withPax(this.pax)
            .withNotes(this.notes)
            .withPlace(this.place)
            .build();
    }

    public static final class ReservationBuilder {
        private Long id;
        private ApplicationUser applicationUser;
        private LocalTime startTime;
        private LocalDate date;
        private LocalTime endTime;
        private Long pax;
        private String notes;
        private Place place;

        private ReservationBuilder() {
        }

        public static ReservationBuilder aReservation() {
            return new ReservationBuilder();
        }

        public ReservationBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ReservationBuilder withUser(ApplicationUser applicationUser) {
            this.applicationUser = applicationUser;
            return this;
        }

        public ReservationBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public ReservationBuilder withPlace(Place place) {
            this.place = place;
            return this;
        }

        public Reservation build() {
            Reservation reservation = new Reservation();
            reservation.setId(id);
            reservation.setUser(applicationUser);
            reservation.setStartTime(startTime);
            reservation.setDate(date);
            reservation.setEndTime(endTime);
            reservation.setPax(pax);
            reservation.setNotes(notes);
            reservation.setPlace(place);
            return reservation;
        }
    }
}
