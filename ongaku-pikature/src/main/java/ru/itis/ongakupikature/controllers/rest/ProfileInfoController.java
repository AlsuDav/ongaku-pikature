package ru.itis.ongakupikature.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.ongakupikature.dto.ProfileInfoDto;
import ru.itis.ongakupikature.service.ProfileService;

@RestController
@RequiredArgsConstructor
public class ProfileInfoController {

    private final ProfileService profileService;

    @GetMapping("/profile/{id}")
    public ProfileInfoDto getProfileInfo(@PathVariable("id") Long userId) {
        return profileService.getProfileInfo(userId);
    }
}
