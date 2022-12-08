package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.NeuroImageComment;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.GenerateImageService;

@RestController
@RequiredArgsConstructor
public class GenerateImageController {

    private final GenerateImageService generateImageService;

    @PostMapping("/neuro_image/generate")
    public String generateImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MusicDto music) {
        return generateImageService.generateImage(music, userDetails.getUser());
    }

    @PostMapping("/neuro_image/comment")
    public String addComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody NeuroImageComment imageComment) {
        return generateImageService.addComment(imageComment, userDetails.getUser());
    }
}
