package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.security.MusicService;
import ru.itis.ongakupikature.security.UserDetailsImpl;

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
            @PathVariable("id") Long playlistsId
    ) {
        return musicService.getPlaylistMusic(playlistsId);
    }

    @PostMapping("/song/{id}/like")
    public ResponseEntity<Void> setLike(
            Authentication authentication,
            @PathVariable("id") Long musicId
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var result = musicService.setLike(userDetails.getUser(), musicId);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
