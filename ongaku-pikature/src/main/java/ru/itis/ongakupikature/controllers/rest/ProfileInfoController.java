package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.security.UserDetailsImpl;
import ru.itis.ongakupikature.service.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileInfoController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public User getProfileInfo(@AuthenticationPrincipal UserDetailsImpl user) {
        return user.getUser();
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<Void> savePhoto(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @RequestParam(value = "image") MultipartFile multipartFile) {
        var result = profileService.saveImage(multipartFile, userDetails.getUser());
        if (result instanceof SaveImageResult.Success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
