package ru.itis.generator;

import lombok.RequiredArgsConstructor;
import ru.itis.dto.Issue;
import ru.itis.dto.Project;
import ru.itis.dto.TestCase;

import java.util.List;

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
        return new Issue(new Project(PROJECT_ID), SUMMARY, DESCRIPTION.formatted(createTableLines(testCases)));
    }

    private String createTableLines(List<TestCase> testsCases) {
        StringBuilder result = new StringBuilder(TABLE_HEADER);
        var caseNumber = 0;

        for (TestCase test : testsCases) {
            result.append(
                    TABLE_LINE.formatted(
                            ++caseNumber,
                            test.getEpic() != null ? test.getEpic() : DEFAULT_VALUE,
                            test.getFeature() != null ? test.getFeature() : DEFAULT_VALUE,
                            test.getStory()  != null ? test.getStory() : DEFAULT_VALUE,
                            test.getName(),
                            test.getStatus(),
                            test.getStatusMessage()
                    )
            );
        }

        return result.toString();
    }
}
