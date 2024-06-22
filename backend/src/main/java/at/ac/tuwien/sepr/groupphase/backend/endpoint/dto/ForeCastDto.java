package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Arrays;
import java.util.Objects;

public class ForeCastDto {
    int[] forecast;
    int maxPlace;

    String[] days;

    public int[] getForecast() {
        return forecast;
    }

    public void setForecast(int[] forecast) {
        this.forecast = forecast;
    }

    public int getMaxPlace() {
        return maxPlace;
    }

    public void setMaxPlace(int maxPlace) {
        this.maxPlace = maxPlace;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ForeCastDto that = (ForeCastDto) object;
        return maxPlace == that.maxPlace && Arrays.equals(forecast, that.forecast) && Arrays.equals(days, that.days);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(maxPlace);
        result = 31 * result + Arrays.hashCode(forecast);
        result = 31 * result + Arrays.hashCode(days);
        return result;
    }

    public ForeCastDto copy() {
        return ForeCastBuilder.aForeCastDto()
            .withForecast(forecast)
            .withMaxPlace(maxPlace)
            .withDays(days)
            .build();
    }

    public static final class ForeCastBuilder {
        int[] forecast;
        int maxPlace;
        String[] days;

        private ForeCastBuilder() {
        }

        public static ForeCastBuilder aForeCastDto() {
            return new ForeCastBuilder();
        }

        public ForeCastBuilder withForecast(int[] forecast) {
            this.forecast = forecast;
            return this;
        }

        public ForeCastBuilder withMaxPlace(int maxPlace) {
            this.maxPlace = maxPlace;
            return this;
        }

        public ForeCastBuilder withDays(String[] days) {
            this.days = days;
            return this;
        }

        public ForeCastDto build() {
            ForeCastDto foreCastDto = new ForeCastDto();
            foreCastDto.setForecast(forecast);
            foreCastDto.setMaxPlace(maxPlace);
            foreCastDto.setDays(days);
            return foreCastDto;
        }
    }
}
