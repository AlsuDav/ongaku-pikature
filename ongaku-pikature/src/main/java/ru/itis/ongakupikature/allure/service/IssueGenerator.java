package ru.itis.ongakupikature.allure.service;

import ru.itis.ongakupikature.allure.dto.Issue;
import ru.itis.ongakupikature.allure.dto.TestCase;

import java.util.List;

public interface IssueGenerator {

    Issue createIssue(List<TestCase> testCases);
}
