package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.PlaylistService;

@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @DeleteMapping("/{login}/playlists/{id}")
    public ResponseEntity<Void> deletePlaylistMusic(
            Authentication authentication,
            @PathVariable String login,
            @PathVariable("id") Long playlistsId,
            @RequestParam("musicId") Long musicId
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var result = playlistService.deleteSongFromPlaylist(userDetails.getUser(), playlistsId, musicId);
        if (result instanceof ActionResult.Success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{login}/playlists/{id}")
    public ResponseEntity<Void> addPlaylistMusic(
            Authentication authentication,
            @PathVariable String login,
            @PathVariable("id") Long playlistsId,
            @RequestParam("musicId") Long musicId
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var result = playlistService.addSongToPlaylist(userDetails.getUser(), playlistsId, musicId);
        if (result instanceof ActionResult.Success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
