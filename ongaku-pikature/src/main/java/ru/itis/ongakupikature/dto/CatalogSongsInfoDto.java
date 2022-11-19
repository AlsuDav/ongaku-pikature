package ru.itis.ongakupikature.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record CatalogSongsInfoDto (
    @JsonProperty("songs_info")
    List<SongsInfoDto> songsInfoDto
){
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
    }

}
