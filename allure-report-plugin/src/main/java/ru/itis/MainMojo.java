package ru.itis;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import ru.itis.generator.IssueGenerator;

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

    IssueGenerator issueGenerator = new IssueGenerator();

    public void execute() throws MojoExecutionException {
        issueGenerator.createIssue(path, uri, token, projectId);
    }
}