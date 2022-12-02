package ru.itis.ongakupikature.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.filestorage.FileStorage;
import ru.itis.ongakupikature.filestorage.dto.FileUuid;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.repository.UsersRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    private static final String LOGIN = "login";
    private static final User EXIST_USER_ID_WITH_PHOTO = User.builder()
            .id(1L)
            .photoPath("/photo.png")
            .login(LOGIN)
            .build();
    private static final MultipartFile MULTIPART_FILE = new MockMultipartFile("filename", "filename", "image", new byte[]{});

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        mockUsersRepository();
    }

    @Test
    void saveImage_loadImageError() {
        Mockito.lenient().when(fileStorage.loadFileToStorage(any()))
                .thenReturn(new LoadResult.Failed.FileNotLoaded("error"));

        var saveImageResult = profileService.saveImage(MULTIPART_FILE, EXIST_USER_ID_WITH_PHOTO);
        checkSaveImageResultError(saveImageResult);
    }

    @Test
    void saveImage_success() {
        Mockito.lenient().when(fileStorage.loadFileToStorage(any()))
                .thenReturn(new LoadResult.Success(new FileUuid("photo.png", "/")));

        var saveImageResult = profileService.saveImage(MULTIPART_FILE, EXIST_USER_ID_WITH_PHOTO);
        checkSaveImageResultSuccess(saveImageResult);
    }

    // Проверка неудачной загрузки фотографии
    private void checkSaveImageResultError(SaveImageResult result) {
        assertThat(result)
                .isInstanceOf(SaveImageResult.Error.class);
    }

    // Проверка удачной загрузки фотографии
    private void checkSaveImageResultSuccess(SaveImageResult result) {
        assertThat(result)
                .isInstanceOf(SaveImageResult.Success.class);
    }

    private void mockUsersRepository() {
        Mockito.lenient().when(usersRepository.save(any()))
                .thenReturn(new User());
    }
}
