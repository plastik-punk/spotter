package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class MessageDto {


    private Long id;

    private LocalDateTime publishedAt;

    private String title;

    private String summary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDto that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(publishedAt, that.publishedAt)
            && Objects.equals(title, that.title)
            && Objects.equals(summary, that.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publishedAt, title, summary);
    }

    @Override
    public String toString() {
        return "MessageDto{"
            + "id=" + id
            + ", publishedAt=" + publishedAt
            + ", title='" + title + '\''
            + ", summary='" + summary + '\''
            + '}';
    }


    public static final class SimpleMessageDtoBuilder {
        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String summary;

        private SimpleMessageDtoBuilder() {
        }

        public static SimpleMessageDtoBuilder aSimpleMessageDto() {
            return new SimpleMessageDtoBuilder();
        }

        public SimpleMessageDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SimpleMessageDtoBuilder withPublishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public SimpleMessageDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public SimpleMessageDtoBuilder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public MessageDto build() {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(id);
            messageDto.setPublishedAt(publishedAt);
            messageDto.setTitle(title);
            messageDto.setSummary(summary);
            return messageDto;
        }
    }
}