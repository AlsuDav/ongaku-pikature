package ru.itis.ongakupikature.controllers.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.ongakupikature.dto.MusicMoreData;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.service.MusicService;
import ru.itis.ongakupikature.security.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

//    @GetMapping("/welcome_page")
//    public List<MusicDto> getAllMusic() {
//        return musicService.getAllMusic();
//    }

//    @GetMapping("/welcome_page")
//    public String getWelcomePage(@AuthenticationPrincipal UserDetailsImpl user, Model model) {
//        model.addAttribute("user", user);
//        model.addAttribute("musicList", musicService.getAllMusic());
//        return "welcome_page";
//    }

    @GetMapping("/{login}/playlists/{id}")
    public List<MusicDto> getPlaylistMusic(
            @PathVariable String login,
            @PathVariable("id") Long playlistsId
    ) {
        return musicService.getPlaylistMusic(playlistsId);
    }

    @PostMapping("/song/{id}/like")
    public ResponseEntity<Void> manageLike(
            Authentication authentication,
            @PathVariable("id") Long musicId,
            @RequestParam("isLike") boolean isLike
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var result = musicService.setLike(userDetails.getUser(), musicId, isLike);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/song/{id}")
    public MusicMoreData getMusicMoreData(
            Authentication authentication,
            @PathVariable("id") Long musicId
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return musicService.getMusicData(userDetails.getUser(), musicId);
    }
}
