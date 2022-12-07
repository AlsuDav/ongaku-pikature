package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.dto.PlaylistDto;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.PlaylistService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping("/{login}/playlists")
    public List<PlaylistDto> getUserPlaylists(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String login
    ) {
        return playlistService.getUserPlaylists(userDetails.getUser());
    }

    @PostMapping("/{login}/playlists")
    public ResponseEntity<Void> createUserPlaylist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String login,
            @RequestParam String name
    ) {
        var result = playlistService.createUserPlaylist(userDetails.getUser(), name);
        if (result instanceof ActionResult.Success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{login}/playlists/{id}")
    public ResponseEntity<Void> deleteUserPlaylist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String login,
            @PathVariable("id") Long playlistId
    ) {
        var result = playlistService.deleteUserPlaylist(userDetails.getUser(), playlistId);
        if (result instanceof ActionResult.Success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
