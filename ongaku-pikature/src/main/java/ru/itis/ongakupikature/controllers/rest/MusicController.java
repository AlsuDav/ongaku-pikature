package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.security.MusicService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @GetMapping("/welcome_page")
    public List<MusicDto> getMainPageMusic() {
        return musicService.getMainPageMusic();
    }
}
