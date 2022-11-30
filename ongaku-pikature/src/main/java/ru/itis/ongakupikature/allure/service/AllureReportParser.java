package ru.itis.ongakupikature.allure.service;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.allure.dto.TestCase;
import ru.itis.ongakupikature.allure.exception.ReadTestCaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
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
        try (var stream = Files.lines(Paths.get(path))) {
            String text = stream.reduce("", String::concat);
            return new Gson().fromJson(text, TestCase.class);
        } catch (IOException e) {
            throw new ReadTestCaseException(e.getMessage());
        }
    }
}
