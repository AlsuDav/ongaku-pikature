package ru.itis.ongakupikature.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.MusicService;

@Controller
@RequiredArgsConstructor
public class WelcomePageController {
    private final MusicService musicService;

    @GetMapping("/welcome_page")
    public String getWelcomePage(@AuthenticationPrincipal UserDetailsImpl user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("musicList", musicService.getAllMusic());
        return "welcome_page";
    }
}
