package ru.itis.ongakupikature.allure.service;

import ru.itis.ongakupikature.allure.dto.TestCase;

import java.nio.file.Path;
import java.util.List;

public interface TestRepostParser {

    List<TestCase> readTestCases(List<Path> paths);
}
