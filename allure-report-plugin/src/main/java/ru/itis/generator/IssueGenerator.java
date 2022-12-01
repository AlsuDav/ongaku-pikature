package ru.itis.generator;

import ru.itis.builder.IssueBuilder;
import ru.itis.builder.YouTrackIssueBuilder;
import ru.itis.parser.AllureReportParser;
import ru.itis.parser.TestRepostParser;
import ru.itis.request.RequestIssue;
import ru.itis.utils.FileLoader;

public class IssueGenerator {

    IssueBuilder issueBuilder = new YouTrackIssueBuilder();
    TestRepostParser testRepostParser = new AllureReportParser();
    RequestIssue requestIssue = new RequestIssue();

    public void createIssue(String path, String uri, String token, String projectId) {
        var paths = FileLoader.getFilesInFolder(path);
        var testCases = testRepostParser.readTestCases(paths);
        var issue = issueBuilder.generateIssue(testCases, projectId);
        requestIssue.sendIssue(uri, token, issue);
    }
}
