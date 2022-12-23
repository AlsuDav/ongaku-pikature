package ru.itis.ongakupikature.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ImageGeneration {

    private static final String SPACE_REPLACING = "%20";

    @Value("${image.generation.url}")
    private String imageGenerationUrl;

    public InputStream generateImageByKeyWords(String keyWords) {
        keyWords = keyWords.replace(" ", SPACE_REPLACING);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(imageGenerationUrl.formatted(keyWords)))
                .GET()
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return response.body();
        } catch (InterruptedException | IOException e) {
            return InputStream.nullInputStream();
        }
    }
}
