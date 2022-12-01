package ru.itis.creator;

import ru.itis.dto.Issue;
import ru.itis.exception.ConvertIssueException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class IssueSender {

    public void sendIssue(String url, Issue issue) {
        var opt = issue.toJson();
        if (opt.isPresent()) {
            createIssue(url, opt.get());
        } else {
            throw new ConvertIssueException("Cannot convert issue %s".formatted(issue));
        }
    }

    private void createIssue(String url, String json) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer perm:Z2dhbGltb3ZhMDY=.NDktMQ==.qrTwIs9S5g6SmBGCVowzwL0Lmq9sKP")
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
