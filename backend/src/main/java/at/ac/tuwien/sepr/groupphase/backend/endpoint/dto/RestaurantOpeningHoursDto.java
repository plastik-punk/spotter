package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class RestaurantOpeningHoursDto {
    private List<LocalTime> monday;
    private List<LocalTime> tuesday;
    private List<LocalTime> wednesday;
    private List<LocalTime> thursday;
    private List<LocalTime> friday;
    private List<LocalTime> saturday;
    private List<LocalTime> sunday;

    public List<LocalTime> getMonday() {
        return monday;
    }

    public RestaurantOpeningHoursDto setMonday(List<LocalTime> monday) {
        this.monday = monday;
        return this;
    }

    public List<LocalTime> getTuesday() {
        return tuesday;
    }

    public RestaurantOpeningHoursDto setTuesday(List<LocalTime> tuesday) {
        this.tuesday = tuesday;
        return this;
    }

    public List<LocalTime> getWednesday() {
        return wednesday;
    }

    public RestaurantOpeningHoursDto setWednesday(List<LocalTime> wednesday) {
        this.wednesday = wednesday;
        return this;
    }

    public List<LocalTime> getThursday() {
        return thursday;
    }

    public RestaurantOpeningHoursDto setThursday(List<LocalTime> thursday) {
        this.thursday = thursday;
        return this;
    }

    public List<LocalTime> getFriday() {
        return friday;
    }

    public RestaurantOpeningHoursDto setFriday(List<LocalTime> friday) {
        this.friday = friday;
        return this;
    }

    public List<LocalTime> getSaturday() {
        return saturday;
    }

    public RestaurantOpeningHoursDto setSaturday(List<LocalTime> saturday) {
        this.saturday = saturday;
        return this;
    }

    public List<LocalTime> getSunday() {
        return sunday;
    }

    public RestaurantOpeningHoursDto setSunday(List<LocalTime> sunday) {
        this.sunday = sunday;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RestaurantOpeningHoursDto that = (RestaurantOpeningHoursDto) object;
        return monday.equals(that.monday)
            && tuesday.equals(that.tuesday)
            && wednesday.equals(that.wednesday)
            && thursday.equals(that.thursday)
            && friday.equals(that.friday)
            && saturday.equals(that.saturday)
            && sunday.equals(that.sunday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @Override
    public String toString() {
        return "RestaurantOpeningHoursDto{"
            + "monday=" + monday
            + ", tuesday=" + tuesday
            + ", wednesday=" + wednesday
            + ", thursday=" + thursday
            + ", friday=" + friday
            + ", saturday=" + saturday
            + ", sunday=" + sunday
            + "}";
    }

    public static final class RestaurantOpeningHoursDtoBuilder {
        private List<LocalTime> monday;
        private List<LocalTime> tuesday;
        private List<LocalTime> wednesday;
        private List<LocalTime> thursday;
        private List<LocalTime> friday;
        private List<LocalTime> saturday;
        private List<LocalTime> sunday;

        private RestaurantOpeningHoursDtoBuilder() {}

        public static RestaurantOpeningHoursDtoBuilder aRestaurantOpeningHoursDto() {
            return new RestaurantOpeningHoursDtoBuilder();
        }

        public RestaurantOpeningHoursDtoBuilder withMonday(List<LocalTime> monday) {
            this.monday = monday;
            return this;
        }

        public RestaurantOpeningHoursDtoBuilder withTuesday(List<LocalTime> tuesday) {
            this.tuesday = tuesday;
            return this;
        }

        public RestaurantOpeningHoursDtoBuilder withWednesday(List<LocalTime> wednesday) {
            this.wednesday = wednesday;
            return this;
        }

        public RestaurantOpeningHoursDtoBuilder withThursday(List<LocalTime> thursday) {
            this.thursday = thursday;
            return this;
        }

        public RestaurantOpeningHoursDtoBuilder withFriday(List<LocalTime> friday) {
            this.friday = friday;
            return this;
        }

        public RestaurantOpeningHoursDtoBuilder withSaturday(List<LocalTime> saturday) {
            this.saturday = saturday;
            return this;
        }

        public RestaurantOpeningHoursDtoBuilder withSunday(List<LocalTime> sunday) {
            this.sunday = sunday;
            return this;
        }

        public RestaurantOpeningHoursDto build() {
            RestaurantOpeningHoursDto restaurantOpeningHoursDto = new RestaurantOpeningHoursDto();
            restaurantOpeningHoursDto.setMonday(monday);
            restaurantOpeningHoursDto.setTuesday(tuesday);
            restaurantOpeningHoursDto.setWednesday(wednesday);
            restaurantOpeningHoursDto.setThursday(thursday);
            restaurantOpeningHoursDto.setFriday(friday);
            restaurantOpeningHoursDto.setSaturday(saturday);
            restaurantOpeningHoursDto.setSunday(sunday);
            return restaurantOpeningHoursDto;
        }
    }
}