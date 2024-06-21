package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Arrays;
import java.util.Objects;

public class UnusualReservationsDto {
    String[] days;
    String[] messages;
    boolean isUnusual;

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public boolean isUnusual() {
        return isUnusual;
    }

    public void setIsUnusual(boolean isUnusual) {
        this.isUnusual = isUnusual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnusualReservationsDto that = (UnusualReservationsDto) o;
        return isUnusual == that.isUnusual && Objects.deepEquals(days, that.days) && Objects.deepEquals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(days), Arrays.hashCode(messages), isUnusual);
    }

    public UnusualReservationsDto copy() {
        return UnusualReservationsBuilder.anUnusualReservationsDto()
            .withDays(days)
            .withMessages(messages)
            .withIsUnusual(isUnusual)
            .build();

    }

    public static final class UnusualReservationsBuilder {
        String[] days;
        String[] messages;
        boolean isUnusual;

        private UnusualReservationsBuilder() {
        }

        public static UnusualReservationsBuilder anUnusualReservationsDto() {
            return new UnusualReservationsBuilder();
        }

        public UnusualReservationsBuilder withDays(String[] days) {
            this.days = days;
            return this;
        }

        public UnusualReservationsBuilder withMessages(String[] messages) {
            this.messages = messages;
            return this;
        }

        public UnusualReservationsBuilder withIsUnusual(boolean isUnusual) {
            this.isUnusual = isUnusual;
            return this;
        }

        public UnusualReservationsDto build() {
            UnusualReservationsDto unusualReservationsDto = new UnusualReservationsDto();
            unusualReservationsDto.setDays(days);
            unusualReservationsDto.setMessages(messages);
            unusualReservationsDto.setIsUnusual(isUnusual);
            return unusualReservationsDto;
        }
    }
}
