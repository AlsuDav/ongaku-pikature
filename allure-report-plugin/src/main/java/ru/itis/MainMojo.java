package ru.itis;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import ru.itis.creator.IssueCreator;
import ru.itis.generator.IssueGenerator;
import ru.itis.generator.YouTrackIssueGenerator;
import ru.itis.parser.AllureReportParser;
import ru.itis.parser.TestRepostParser;
import ru.itis.utils.FileLoader;

@Mojo(name = "createissue")
public class MainMojo extends AbstractMojo {

    @Parameter(property = "allure-report.test-cases.folder", defaultValue = "")
    private String path;

    @Parameter(property = "you-track.uri", defaultValue = "")
    private String uri;

    @Parameter(property = "you-track.token", defaultValue = "")
    private String token;

    @Parameter(property = "you-track.project.id", defaultValue = "")
    private String projectId;

    IssueGenerator issueGenerator = new YouTrackIssueGenerator();
    TestRepostParser testRepostParser = new AllureReportParser();
    IssueCreator issueCreator = new IssueCreator();
    public void execute() throws MojoExecutionException {
        var paths = FileLoader.getFilesInFolder(path);
        var testCases = testRepostParser.readTestCases(paths);
        var issue = issueGenerator.generateIssue(testCases, projectId);
        issueCreator.sendIssue(uri, token, issue);
    }
}