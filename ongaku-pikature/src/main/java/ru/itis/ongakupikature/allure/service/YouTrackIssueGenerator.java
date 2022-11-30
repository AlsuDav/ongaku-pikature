package ru.itis.ongakupikature.allure.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.allure.dto.Issue;
import ru.itis.ongakupikature.allure.dto.TestCase;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

@Service
@RequiredArgsConstructor
public class YouTrackIssueGenerator implements IssueGenerator {

    private static final String PROJECT_ID = "0-1";
    private static final String SUMMARY = "Неуспешные тесты";
    private static final String DESCRIPTION = "Исправить тесты:\n\n%s";
    private static final String DEFAULT_VALUE = "";
    private static final String TABLE_LINE = "| %d | %s | %s | %s | %s | %s | %s |\n";
    private static final String TABLE_HEADER = "| № | Epic | Feature | Story | Test | Result | Error |\n| --- | --- | --- | --- | --- | --- | -- |\n";

    @Override
    public Issue createIssue(List<TestCase> testCases) {
        var notPassed = testCases.stream()
                .sorted(Comparator.comparing(TestCase::getStatus)
                        .thenComparing(TestCase::getEpic, nullsFirst(naturalOrder()))
                        .thenComparing(TestCase::getFeature, nullsFirst(naturalOrder()))
                        .thenComparing(TestCase::getStory, nullsFirst(naturalOrder()))
                        .thenComparing(TestCase::getName, nullsFirst(naturalOrder())))
                .toList();

        return Issue.builder()
                .project(new Issue.Project(PROJECT_ID))
                .summary(SUMMARY)
                .description(DESCRIPTION.formatted(createTableLines(notPassed)))
                .build();
    }

    private String createTableLines(List<TestCase> testsCases) {
        StringBuilder result = new StringBuilder(TABLE_HEADER);
        var caseNumber = 0;

        for (TestCase test : testsCases) {
            result.append(
                    TABLE_LINE.formatted(
                            ++caseNumber,
                            Strings.isNotEmpty(test.getEpic()) ? test.getEpic() : DEFAULT_VALUE,
                            Strings.isNotEmpty(test.getFeature()) ? test.getFeature() : DEFAULT_VALUE,
                            Strings.isNotEmpty(test.getStory()) ? test.getStory() : DEFAULT_VALUE,
                            test.getName(),
                            test.getStatus(),
                            test.getStatusMessage()
                    )
            );
        }

        return result.toString();
    }
}
