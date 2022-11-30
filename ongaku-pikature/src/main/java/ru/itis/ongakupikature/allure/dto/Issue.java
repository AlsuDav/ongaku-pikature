package ru.itis.ongakupikature.allure.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Builder;

import java.util.Optional;

@Builder
public record Issue(
        Project project,
        String summary,
        String description
) {

    public record Project(
            String id
    ) { }

    public Optional<String> toJson() {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            return Optional.of(ow.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
