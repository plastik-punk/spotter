package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AdminViewDto {

    @NotEmpty(message = "Area is required")
    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß ]+$")
    String area;

    @NotEmpty(message = "Start time is required")
    LocalTime startTime;

    @NotEmpty(message = "Date is required")
    LocalDate date;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AdminViewDto that = (AdminViewDto) object;
        return Objects.equals(area, that.area) && Objects.equals(startTime, that.startTime) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(area, startTime, date);
    }

    @Override
    public String toString() {
        return "AdminViewDto{"
            + "area='" + area + '\''
            + ", startTime=" + startTime
            + ", date=" + date
            + '}';
    }

    public AdminViewDto copy() {
        return AdminViewBuilder.anAdminViewDto()
            .withArea(area)
            .withStartTime(startTime)
            .withDate(date)
            .build();
    }

    public static final class AdminViewBuilder {
        private String area;
        private LocalTime startTime;
        private LocalDate date;

        public AdminViewBuilder() {
        }

        public static AdminViewBuilder anAdminViewDto() {
            return new AdminViewBuilder();
        }

        public AdminViewBuilder withArea(String area) {
            this.area = area;
            return this;
        }

        public AdminViewBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public AdminViewBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public AdminViewDto build() {
            AdminViewDto adminViewDto = new AdminViewDto();
            adminViewDto.setArea(area);
            adminViewDto.setStartTime(startTime);
            adminViewDto.setDate(date);
            return adminViewDto;
        }
    }
}
