package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AdminViewDto {

    @NotNull(message = "AreaId is required")
    Long areaId;

    @NotNull(message = "startTime is required")
    LocalTime startTime;

    @NotNull(message = "Date must not be null")
    LocalDate date;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
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
        return Objects.equals(areaId, that.areaId) && Objects.equals(startTime, that.startTime) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaId, startTime, date);
    }

    @Override
    public String toString() {
        return "AdminViewDto{"
            + "areaId='" + areaId + '\''
            + ", startTime=" + startTime
            + ", date=" + date
            + '}';
    }

    public AdminViewDto copy() {
        return AdminViewBuilder.anAdminViewDto()
            .withAreaId(areaId)
            .withStartTime(startTime)
            .withDate(date)
            .build();
    }

    public static final class AdminViewBuilder {
        private Long areaId;
        private LocalTime startTime;
        private LocalDate date;

        public AdminViewBuilder() {
        }

        public static AdminViewBuilder anAdminViewDto() {
            return new AdminViewBuilder();
        }

        public AdminViewBuilder withAreaId(Long areaId) {
            this.areaId = areaId;
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
            adminViewDto.setAreaId(areaId);
            adminViewDto.setStartTime(startTime);
            adminViewDto.setDate(date);
            return adminViewDto;
        }
    }
}
