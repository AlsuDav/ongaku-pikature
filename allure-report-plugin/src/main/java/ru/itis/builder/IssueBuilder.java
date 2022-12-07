package ru.itis.builder;

import ru.itis.dto.Issue;
import ru.itis.dto.TestCase;

import java.util.List;

public interface IssueBuilder {

    Issue generateIssue(List<TestCase> testCases, String projectId);
}
