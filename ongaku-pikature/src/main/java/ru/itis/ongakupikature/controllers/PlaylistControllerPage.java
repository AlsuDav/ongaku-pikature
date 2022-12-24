package ru.itis.ongakupikature.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.MusicService;
import ru.itis.ongakupikature.service.PlaylistService;

@Controller
@RequiredArgsConstructor
public class PlaylistControllerPage {

    private final PlaylistService playlistService;
    private final MusicService musicService;


    @GetMapping("/{login}/playlists")
    public String getUserPlaylists(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String login,
            Model model

    ) {
        var userPlaylists = playlistService.getUserPlaylists(userDetails.getUser());
        model.addAttribute("userPlaylists", userPlaylists);
        model.addAttribute("user", userDetails);
        return "user_playlists";
    }
    @GetMapping("/{login}/playlists/{id}")
    public String getPlaylistMusic(
            @PathVariable String login,
            @PathVariable("id") Long playlistsId,
            @AuthenticationPrincipal UserDetailsImpl user,
            Model model
    ) {
        model.addAttribute("playlistMusic", musicService.getPlaylistMusic(playlistsId));
        model.addAttribute("user", user);
        model.addAttribute("playlistInfo", playlistService.getPlaylistById(playlistsId));
        var favouriteMusic = musicService.getPlaylistMusic(user.getUser().getFavoritePlaylistId());
        model.addAttribute("favouriteMusicIds", favouriteMusic.stream().map(MusicDto::id).toList());
        return "playlist";
    }

    @PostMapping("/{login}/playlists")
    public String createUserPlaylist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String login,
            String name
    ) {
        var result = playlistService.createUserPlaylist(userDetails.getUser(), name);
        if (result instanceof ActionResult.Success) {
            return "redirect:/" + login + "/playlists";
        }
        return "redirect:/" + login + "/playlists";
    }

    @RequestMapping(
            value = "/{login}/playlists/{id}",
            method = RequestMethod.POST

    )
    public String addPlaylistMusic(
            Authentication authentication,
            @PathVariable String login,
            @PathVariable("id") Long musicId,
            Long playlistsId
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        playlistService.addSongToPlaylist(userDetails.getUser(), playlistsId, musicId);

        return "redirect:/" + login + "/playlists/" + playlistsId;
    }
}
