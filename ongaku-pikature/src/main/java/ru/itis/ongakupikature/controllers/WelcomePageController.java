package ru.itis.ongakupikature.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.MusicService;
import ru.itis.ongakupikature.service.PlaylistService;

@Controller
@RequiredArgsConstructor
public class WelcomePageController {
    private final MusicService musicService;
    private final PlaylistService playlistService;

    @GetMapping("/welcome_page")
    public String getWelcomePage(@AuthenticationPrincipal UserDetailsImpl user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("musicList", musicService.getAllMusic());
        if (user != null) {
            var userPlaylists = playlistService.getUserPlaylists(user.getUser());
            model.addAttribute("userPlaylists", userPlaylists);
            var favouriteMusic = musicService.getPlaylistMusic(user.getUser().getFavoritePlaylistId());
            model.addAttribute("favouriteMusicIds", favouriteMusic.stream().map(MusicDto::id).toList());
        }
        return "welcome_page";
    }
}
