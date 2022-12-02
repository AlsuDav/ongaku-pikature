package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.filestorage.FileStorage;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.filestorage.dto.UploadParams;
import ru.itis.ongakupikature.repository.UsersRepository;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UsersRepository usersRepository;
    private final FileStorage fileStorage;

    public SaveImageResult saveImage(MultipartFile multipartFile, User user) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
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
