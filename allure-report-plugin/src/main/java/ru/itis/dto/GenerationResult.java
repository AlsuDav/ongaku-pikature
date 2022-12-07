package ru.itis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.itis.enums.LogLevel;

@Getter
@AllArgsConstructor
public class GenerationResult {
    private final LogLevel level;
    private final String message;
}
