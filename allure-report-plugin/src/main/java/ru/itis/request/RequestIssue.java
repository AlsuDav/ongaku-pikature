package ru.itis.request;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestIssue {

    public void sendIssue(String url, String token, String issue) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer %s".formatted(token))
                .POST(HttpRequest.BodyPublishers.ofString(issue))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            System.out.printf("Cannot send request to uri %s%n", url);
            Thread.currentThread().interrupt();
        }
    }
}
