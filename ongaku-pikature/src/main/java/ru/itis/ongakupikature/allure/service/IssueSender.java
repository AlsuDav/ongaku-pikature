package ru.itis.ongakupikature.allure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.allure.dto.Issue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class IssueSender {

    public void sendIssue(String url, Issue issue) {
        issue.toJson().ifPresentOrElse(json -> createIssue(url, json),
                () -> log.error("Cannot convert issue {}", issue));
    }

    private void createIssue(String url, String json) {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer perm:Z2dhbGltb3ZhMDY=.NDktMQ==.qrTwIs9S5g6SmBGCVowzwL0Lmq9sKP")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            log.error("Cannot send request to uri {}", url);
            Thread.currentThread().interrupt();
        }
    }
}
