package ru.itis.ongakupikature.allure.service;

import com.google.gson.Gson;
import ru.itis.ongakupikature.allure.dto.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AllureReportParser {

    private static final String SUCCESS_STATUS = "passed";

    private AllureReportParser() {
        throw new IllegalStateException("Utility class");
    }

    public static TestCase readTestCasesJson(String path) {
        try (var stream = Files.lines(Paths.get(path))) {
            String text = stream.reduce("", String::concat);
            return new Gson().fromJson(text, TestCase.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<Path> getFilesInFolder(String path) throws IOException {
        var pathObject = Paths.get(path);
        try (var paths = Files.walk(pathObject)) {
            return paths
                    .filter(Files::isRegularFile)
                    .toList();
        }
    }

    public static List<TestCase> getTestCasesDataInFolder(List<Path> paths) {
        List<TestCase> testCases = new ArrayList<>();
        TestCase testCase;

        for (Path path : paths) {
            testCase = readTestCasesJson(path.toString());

            if (!testCase.getStatus().equals(SUCCESS_STATUS) ) {
                testCase.initLabels();
                testCases.add(testCase);
            }
        }

        return testCases;
    }
}
