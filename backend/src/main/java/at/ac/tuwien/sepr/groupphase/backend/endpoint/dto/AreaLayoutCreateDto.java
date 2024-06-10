package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;

public class AreaLayoutCreateDto {
    private String name;
    private Integer width;
    private Integer height;
    private boolean isOpen;
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

    public List<PlaceVisualDto> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceVisualDto> places) {
        this.places = places;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public static class PlaceDto {

        private Long numberOfSeats;
        private List<CoordinateDto> coordinates;

        // Getters and Setters

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
            private Integer x1;
            private Integer y1;

            // Getters and Setters

            public Integer getX() {
                return x1;
            }

            public void setX(Integer x1) {
                this.x1 = x1;
            }

            public Integer getY() {
                return y1;
            }

            public void setY(Integer y1) {
                this.y1 = y1;
            }
        }
    }
}
