package ru.itis.ongakupikature.allure.dto;

import lombok.Builder;

@Builder
public record Issue(
        Project project,
        String summary,
        String description
) {

    public record Project(
            String id
    ) { }
}
