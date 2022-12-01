package ru.itis.creator;

import ru.itis.dto.Issue;
import ru.itis.utils.JsonHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class IssueCreator {

    public void sendIssue(String url, String token, Issue issue) {
        var opt = JsonHandler.serializeIssue(issue);
        createIssue(url, token, opt);
    }

    private void createIssue(String url, String token, String json) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer %s".formatted(token))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            System.out.printf("Cannot send request to uri %s%n", url);
            Thread.currentThread().interrupt();
        }
    }
}
