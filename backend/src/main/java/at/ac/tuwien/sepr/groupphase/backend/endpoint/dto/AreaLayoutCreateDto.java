package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;

public class AreaLayoutCreateDto {
    private String name;
    private Integer width;
    private Integer height;
    private boolean isOpen;
    private boolean isMainArea;
    private String openingTime;
    private String closingTime;
    private List<PlaceVisualDto> places;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isMainArea() {
        return isMainArea;
    }

    public void setMainArea(boolean isMainArea) {
        this.isMainArea = isMainArea;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public List<PlaceVisualDto> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceVisualDto> places) {
        this.places = places;
    }

    public static class PlaceVisualDto {
        private Integer placeNumber;
        private Boolean status;
        private Boolean reservation;
        private Long numberOfSeats;
        private List<CoordinateDto> coordinates;

        // Getters and Setters

        public Integer getPlaceNumber() {
            return placeNumber;
        }

        public void setPlaceNumber(Integer placeNumber) {
            this.placeNumber = placeNumber;
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

        public static class CoordinateDto {
            private Integer x;
            private Integer y;

            // Getters and Setters

            public Integer getX() {
                return x;
            }

            public void setX(Integer x) {
                this.x = x;
            }

            public Integer getY() {
                return y;
            }

            public void setY(Integer y) {
                this.y = y;
            }
        }
    }
}
