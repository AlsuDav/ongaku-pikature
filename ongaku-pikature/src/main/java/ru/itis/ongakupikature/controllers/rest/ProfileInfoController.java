package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.ongakupikature.dto.ProfileInfoDto;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.service.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileInfoController {

    private final ProfileService profileService;

    @GetMapping("/profile/{id}")
    public ProfileInfoDto getProfileInfo(@PathVariable("id") Long userId) {
        return profileService.getProfileInfo(userId);
    }

    @PostMapping("/profile/{id}/photo")
    public ResponseEntity<Void> savePhoto(@PathVariable("id") Long userId,
                                    @RequestParam(value = "image") MultipartFile multipartFile) {
        var result = profileService.saveImage(multipartFile, userId);
        if (result instanceof SaveImageResult.Success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
