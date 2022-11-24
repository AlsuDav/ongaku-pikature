package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.ongakupikature.dto.ProfileInfoDto;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.filestorage.FileStorage;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.filestorage.dto.UploadParams;
import ru.itis.ongakupikature.repository.UsersRepository;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Value("${profile.image.path}")
    private String defaultImagePath;

    private final UsersRepository usersRepository;
    private final FileStorage fileStorage;

    public ProfileInfoDto getProfileInfo(Long id) {
        var optionalUser = usersRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        var user = optionalUser.get();
        var photoPath = user.getPhotoPath() == null ? defaultImagePath : user.getPhotoPath();
        return new ProfileInfoDto(user.getId(), user.getLogin(), photoPath);
    }

    public SaveImageResult saveImage(MultipartFile multipartFile, Long userId) {
        var optionalUser = usersRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new SaveImageResult.Error();
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            var user = optionalUser.get();
            var loadResult = fileStorage.loadFileToStorage(UploadParams.builder()
                    .fileInputStream(inputStream)
                    .fileName(multipartFile.getOriginalFilename())
                    .build());
            if (loadResult instanceof LoadResult.Success) {
                var fileUuid = loadResult.fileUuid();
                user.setPhotoPath(fileUuid.path() + fileUuid.uuid());
                usersRepository.save(user);
                return new SaveImageResult.Success();
            }
            return new SaveImageResult.Error();
        } catch (IOException e) {
            return new SaveImageResult.Error();
        }
    }
}
