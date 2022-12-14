package ru.itis.ongakupikature.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.ongakupikature.security.UserDetailsImpl;

@Controller
public class ProfileController {

//    @GetMapping("/profile")
//    public String getProfilePage(@AuthenticationPrincipal UserDetailsImpl user, Model model) {
//        model.addAttribute("user", user);
//        return "profile";
//    }


}
