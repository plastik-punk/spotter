package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;

public class AreaDetailDto {

    private Long id;
    private String name;
    private boolean isMainArea;
    private String openingTime;
    private String closingTime;
    private boolean isOpen;
    private Integer width;
    private Integer height;
    private List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> places;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMainArea() {
        return isMainArea;
    }

    public void setMainArea(boolean mainArea) {
        isMainArea = mainArea;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> getPlaces() {
        return places;
    }

    public void setPlaces(List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> places) {
        this.places = places;
    }
}
