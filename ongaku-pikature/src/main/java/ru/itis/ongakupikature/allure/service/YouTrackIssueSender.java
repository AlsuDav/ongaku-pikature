package ru.itis.ongakupikature.allure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.HttpHeaders;
import ru.itis.ongakupikature.allure.dto.Issue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class YouTrackIssueSender {

    private YouTrackIssueSender() {
        throw new IllegalStateException("Utility class");
    }

    private static final String PROJECT_ID = "0-1";
    private static final String SUMMARY = "Неуспешные тесты";
    private static final String DESCRIPTION = "Исправить тесты:\n\n%s";

    public static void createConfluenceTable(String url, String table) throws IOException, InterruptedException {
        var issue = Issue.builder()
                .project(new Issue.Project(PROJECT_ID))
                .summary(SUMMARY)
                .description(DESCRIPTION.formatted(table))
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(issue);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

//        var path = "/home/gulshat/IdeaProjects/main-ongaku-pikature/ongaku-pikature/allure-report/data/test-cases";
//        var table = ErrorTableCreator.createTableBody(path);
//        YouTrackIssueSender.createConfluenceTable("https://ongaku-pikature.youtrack.cloud/api/issues", table);
    }
}
