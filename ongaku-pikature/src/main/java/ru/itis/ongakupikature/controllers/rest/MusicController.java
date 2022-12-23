package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.ongakupikature.service.MusicService;
import ru.itis.ongakupikature.security.UserDetailsImpl;


@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

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

}
