package ru.itis.ongakupikature.allure.service;

import org.apache.logging.log4j.util.Strings;
import ru.itis.ongakupikature.allure.dto.TestCase;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static ru.itis.ongakupikature.allure.service.AllureReportParser.getFilesInFolder;
import static ru.itis.ongakupikature.allure.service.AllureReportParser.getTestCasesDataInFolder;

public class ErrorTableCreator {

    private ErrorTableCreator() {
        throw new IllegalStateException("Utility class");
    }

    static final String TABLE_LINE = "| %d | %s | %s | %s | %s | %s | %s |\n";
    static final String TABLE_HEADER = "| № | Epic | Feature | Story | Test | Result | Error |\n| --- | --- | --- | --- | --- | --- | -- |\n";

    public static String createTableBody(String testCasesPath) throws IOException {

        List<Path> filesPath = getFilesInFolder(testCasesPath);
        List<TestCase> tests = getTestCasesDataInFolder(filesPath);

        List<TestCase> notPassed = tests.stream()
                .sorted(Comparator.comparing(TestCase::getStatus)
                        .thenComparing(TestCase::getEpic, nullsFirst(naturalOrder()))
                        .thenComparing(TestCase::getFeature, nullsFirst(naturalOrder()))
                        .thenComparing(TestCase::getStory, nullsFirst(naturalOrder()))
                        .thenComparing(TestCase::getName, nullsFirst(naturalOrder())))
                .toList();

        return createTableLines(notPassed);
    }

    private static String createTableLines(List<TestCase> testsCases) {
        StringBuilder result = new StringBuilder(TABLE_HEADER);
        var caseNumber = 0;

        for (TestCase test : testsCases) {

            result.append(
                    TABLE_LINE.formatted(
                            ++caseNumber,
                            Strings.isNotEmpty(test.getEpic()) ? test.getEpic() : "",
                            Strings.isNotEmpty(test.getFeature()) ? test.getFeature() : "",
                            Strings.isNotEmpty(test.getStory()) ? test.getStory() : "",
                            test.getName(),
                            test.getStatus(),
                            test.getStatusMessage()
                    )
            );
        }

        return result.toString();
    }
}
