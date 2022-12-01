package ru.itis.parser;

import ru.itis.dto.TestCase;
import ru.itis.exception.ReadTestCaseException;
import ru.itis.utils.JsonHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AllureReportParser implements TestRepostParser {

    private static final String SUCCESS_STATUS = "passed";

    @Override
    public List<TestCase> readTestCases(List<Path> paths) {
        List<TestCase> testCases = new ArrayList<>();
        TestCase testCase;

        for (Path path : paths) {
            testCase = readTestCaseJson(path.toString());

            if (!testCase.getStatus().equals(SUCCESS_STATUS) ) {
                testCase.initLabels();
                testCases.add(testCase);
            }
        }

        return testCases;
    }

    private TestCase readTestCaseJson(String path) {
        try {
            var testCaseJson = new String(Files.readAllBytes(Paths.get(path)));
            var testCase = JsonHandler.deserializeIssue(testCaseJson);
            testCase.initLabels();
            return testCase;
        } catch (IOException e) {
            throw new ReadTestCaseException(e.getMessage());
        }
    }
}
