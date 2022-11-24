package ru.itis.ongakupikature.dto;

import lombok.Builder;


@Builder
public record ProfileInfoDto(
        Long id,
        String login,
        String photoPath
) {
}
