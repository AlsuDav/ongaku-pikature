package ru.itis.ongakupikature.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.MusicService;

@Controller
@RequiredArgsConstructor
public class MusicPageController {

    private final MusicService musicService;

    @GetMapping("/song/{id}")
    public String getMusicMoreData(
            Authentication authentication,
            @PathVariable("id") Long musicId,
            Model model
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        model.addAttribute("musicAddedData", musicService.getMusicData(userDetails.getUser(), musicId));
        model.addAttribute("musicMainData", musicService.getMusicById(musicId));
        model.addAttribute("user", userDetails.getUser());
        return "song_page";
    }
}
