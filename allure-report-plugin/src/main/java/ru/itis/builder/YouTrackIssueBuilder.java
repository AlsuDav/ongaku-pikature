package ru.itis.builder;

import lombok.RequiredArgsConstructor;
import ru.itis.dto.Issue;
import ru.itis.dto.Project;
import ru.itis.dto.TestCase;

import java.util.List;

@RequiredArgsConstructor
public class YouTrackIssueBuilder implements IssueBuilder {

    private static final String SUMMARY = "Неуспешные тесты";
    private static final String DESCRIPTION = "Исправить тесты:\n\n%s";
    private static final String DEFAULT_VALUE = "";
    private static final String TABLE_LINE = "| %d | %s | %s | %s | %s | %s | %s |\n";
    private static final String TABLE_HEADER = "| № | Epic | Feature | Story | Test | Result | Error |\n| --- | --- | --- | --- | --- | --- | -- |\n";

    @Override
    public Issue generateIssue(List<TestCase> testCases, String projectId) {
        var tableLines = createTableLines(testCases);
        return new Issue(new Project(projectId), SUMMARY, DESCRIPTION.formatted(tableLines));
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
                            test.getStory() != null ? test.getStory() : DEFAULT_VALUE,
                            test.getName(),
                            test.getStatus().getColoredStatus(),
                            test.getStatusMessage()
                    )
            );
        }

        return result.toString();
    }
}