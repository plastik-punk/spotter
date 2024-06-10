package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

public class PlaceVisualDto {

    private Long placeId;
    private Boolean status;
    private Boolean reservation;
    private Long numberOfSeats;
    private Integer number;
    private List<CoordinateDto> coordinates;

    // Getters and Setters

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getReservation() {
        return reservation;
    }

    public void setReservation(Boolean reservation) {
        this.reservation = reservation;
    }

    public Long getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Long numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public List<CoordinateDto> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<CoordinateDto> coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlaceVisualDto that = (PlaceVisualDto) o;
        return Objects.equals(placeId, that.placeId)
            && Objects.equals(status, that.status)
            && Objects.equals(reservation, that.reservation)
            && Objects.equals(numberOfSeats, that.numberOfSeats)
            && Objects.equals(coordinates, that.coordinates)
            && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, status, reservation, numberOfSeats, coordinates, number);
    }

    @Override
    public String toString() {
        return "PlaceVisualDto{"
            + "placeId="
            + placeId
            + ", status="
            + status
            + ", reservation="
            + reservation
            + ", numberOfSeats="
            + numberOfSeats
            + ", coordinates="
            + coordinates
            + ", number="
            + number
            + '}';
    }

    public static class CoordinateDto {

        private Integer x1;
        private Integer y1;

        // Getters and Setters
        public Integer getX() {
            return x1;
        }

        public void setX(Integer x) {
            this.x1 = x;
        }

        public Integer getY() {
            return y1;
        }

        public void setY(Integer y) {
            this.y1 = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CoordinateDto that = (CoordinateDto) o;
            return Objects.equals(x1, that.x1)
                && Objects.equals(y1, that.y1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x1, y1);
        }

        @Override
        public String toString() {
            return "CoordinateDto{"
                + "x="
                + x1
                + ", y="
                + y1
                + '}';
        }
    }
}