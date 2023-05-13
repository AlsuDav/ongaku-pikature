package ru.itis.ongakupikature.dto;

import lombok.Builder;

@Builder
public record PlaylistDto(
        Long id,
        String name,
        Integer songCount
) {
}
