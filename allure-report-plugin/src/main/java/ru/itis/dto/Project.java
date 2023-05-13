package ru.itis.dto;

import lombok.Getter;

@Getter
public class Project {
    private final String id;

    public Project(String id) {
        this.id = id;
    }
}
