package ru.itis.generator;

import ru.itis.dto.Issue;
import ru.itis.dto.TestCase;

import java.util.List;

public interface IssueGenerator {

    Issue generateIssue(List<TestCase> testCases, String projectId);
}
