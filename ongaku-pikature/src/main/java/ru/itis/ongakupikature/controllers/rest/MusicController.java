package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.security.MusicService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @GetMapping("/welcome_page")
    public List<MusicDto> getAllMusic() {
        return musicService.getAllMusic();
    }

    @GetMapping("/{login}/playlists/{id}")
    public List<MusicDto> getPlaylistMusic(
            @PathVariable String login,
            @PathVariable Long id
    ) {
        return musicService.getPlaylistMusic(id);
    }
}
