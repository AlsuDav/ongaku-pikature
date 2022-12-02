package ru.itis.ongakupikature.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MusicDto(
        Long id,
        String name,
        List<String> authors,
        String picturePath,
        String musicPath
) {
}
