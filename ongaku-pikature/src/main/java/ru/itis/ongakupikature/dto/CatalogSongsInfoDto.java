package ru.itis.ongakupikature.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Builder
public record CatalogSongsInfoDto(
        @JsonProperty("songs_info")
        List<SongsInfoDto> songsInfoDto
) {
    @Builder
    public record SongsInfoDto(
            @JsonProperty("author_name")
            String authorName,
            @JsonProperty("name")
            String name,
            @JsonProperty("text")
            String text,
            @JsonProperty("significant_words")
            String[] significantWords
    ) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SongsInfoDto songsInfoDto = (SongsInfoDto) o;
            return Arrays.equals(significantWords, songsInfoDto.significantWords);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(significantWords);
            result = 31 * result + Arrays.hashCode(significantWords);
            return result;
        }

        @Override
        public String toString() {
            return "SongsInfoDto{" +
                    ", author_name=" + authorName +
                    ", name=" + name +
                    ", text=" + text +
                    "significantWords=" + Arrays.toString(significantWords) +
                    '}';
        }
    }

}


