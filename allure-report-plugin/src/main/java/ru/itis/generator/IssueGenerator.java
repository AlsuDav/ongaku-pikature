package ru.itis.generator;

import ru.itis.builder.IssueBuilder;
import ru.itis.builder.YouTrackIssueBuilder;
import ru.itis.dto.GenerationResult;
import ru.itis.enums.LogLevel;
import ru.itis.parser.AllureReportParser;
import ru.itis.parser.TestRepostParser;
import ru.itis.request.RequestIssue;
import ru.itis.utils.FileLoader;
import ru.itis.utils.JsonHandler;

public class IssueGenerator {

    IssueBuilder issueBuilder = new YouTrackIssueBuilder();
    TestRepostParser testRepostParser = new AllureReportParser();
    RequestIssue requestIssue = new RequestIssue();

    public GenerationResult createIssue(String path, String uri, String token, String projectId) {
        var paths = FileLoader.getFilesInFolder(path);
        if (paths.isEmpty()) {
            return new GenerationResult(LogLevel.WARN, "allure-report test-cases not found");
        }
        var testCases = testRepostParser.readTestCases(paths);
        var issue = issueBuilder.generateIssue(testCases, projectId);
        requestIssue.sendIssue(uri, token, JsonHandler.serializeIssue(issue));
        return null;
    }
}
