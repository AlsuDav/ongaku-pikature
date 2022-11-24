package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.ProfileInfoDto;
import ru.itis.ongakupikature.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Value("${profile.image.path}")
    private String defaultImagePath;

    private final UsersRepository usersRepository;

    public ProfileInfoDto getProfileInfo(Long id) {
        var optionalUser = usersRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        var user = optionalUser.get();
        var photoPath = user.getPhotoPath() == null ? defaultImagePath : user.getPhotoPath();
        return new ProfileInfoDto(user.getId(), user.getLogin(), photoPath);
    }
}
