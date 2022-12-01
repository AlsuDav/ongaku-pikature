package ru.itis.parser;

import ru.itis.dto.TestCase;

import java.nio.file.Path;
import java.util.List;

public interface TestRepostParser {

    List<TestCase> readTestCases(List<Path> paths);
}
