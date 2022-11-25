package ru.itis.ongakupikature.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.ongakupikature.dto.ProfileInfoDto;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.filestorage.FileStorage;
import ru.itis.ongakupikature.filestorage.dto.FileUuid;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.repository.UsersRepository;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    private static final Long EXIST_USER_ID_WITH_PHOTO = 1L;
    private static final Long EXIST_USER_ID_WITHOUT_PHOTO = 2L;
    private static final Long NOT_EXIST_USER_ID = 3L;
    private static final String LOGIN = "login";
    private static final String DEFAULT_PHOTO_PATH = "/image.png";
    private static final MultipartFile MULTIPART_FILE = new MockMultipartFile("filename", "filename", "image", new byte[] {});

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        mockUsersRepository();
        ReflectionTestUtils.setField(profileService, "defaultImagePath", DEFAULT_PHOTO_PATH);
    }

    @Test
    void getProfileInfo_userWithoutPhotoFound() {
        var profileInfo = profileService.getProfileInfo(EXIST_USER_ID_WITHOUT_PHOTO);
        var expected = ProfileInfoDto.builder()
                .id(EXIST_USER_ID_WITHOUT_PHOTO)
                .login(LOGIN)
                .photoPath(DEFAULT_PHOTO_PATH)
                .build();
        checkProfileInfoFound(profileInfo);
        checkProfileInfo(profileInfo, expected);
        checkPhotoPath(profileInfo.photoPath(), expected.photoPath());
    }

    @Test
    void getProfileInfo_userWithPhotoFound() {
        var profileInfo = profileService.getProfileInfo(EXIST_USER_ID_WITH_PHOTO);
        var expected = ProfileInfoDto.builder()
                .id(EXIST_USER_ID_WITH_PHOTO)
                .login(LOGIN)
                .photoPath("/photo.png")
                .build();
        checkProfileInfoFound(profileInfo);
        checkProfileInfo(profileInfo, expected);
        checkPhotoPath(profileInfo.photoPath(), expected.photoPath());
    }

    @Test
    void getProfileInfo_userNotFound() {
        var profileInfo = profileService.getProfileInfo(NOT_EXIST_USER_ID);
        checkProfileInfoNotFound(profileInfo);
    }

    @Test
    void saveImage_userNotFound() {
        var saveImageResult = profileService.saveImage(MULTIPART_FILE, NOT_EXIST_USER_ID);
        checkSaveImageResultError(saveImageResult);
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

    // Проверка эквивалентности действительного пути до фото ожидаемому
    private void checkPhotoPath(String actualPath, String expectedPath) {
        assertThat(actualPath)
                .isNotNull()
                .isEqualTo(expectedPath);
    }

    // Проверка эквивалентности действительной информации профиля ожидаемому
    private void checkProfileInfo(ProfileInfoDto actualProfileInfo, ProfileInfoDto expectedProfileInfo) {
        assertThat(actualProfileInfo)
                .satisfies(pi -> {
                    assertThat(pi.id())
                            .isEqualTo(expectedProfileInfo.id());
                    assertThat(pi.login())
                            .isEqualTo(expectedProfileInfo.login());
                });
    }

    // Проверка нахождения пользователя
    private void checkProfileInfoFound(ProfileInfoDto profileInfo) {
        assertThat(profileInfo)
                .isNotNull();
    }

    // Проверка не нахождения пользователя
    private void checkProfileInfoNotFound(ProfileInfoDto profileInfo) {
        assertThat(profileInfo)
                .isNull();
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
        Mockito.lenient().when(usersRepository.findById(EXIST_USER_ID_WITH_PHOTO))
                .thenReturn(Optional.of(User.builder()
                        .id(EXIST_USER_ID_WITH_PHOTO)
                        .login(LOGIN)
                        .photoPath("/photo.png")
                        .build()));
        Mockito.lenient().when(usersRepository.findById(EXIST_USER_ID_WITHOUT_PHOTO))
                .thenReturn(Optional.of(User.builder()
                        .id(EXIST_USER_ID_WITHOUT_PHOTO)
                        .login(LOGIN)
                        .build()));
        Mockito.lenient().when(usersRepository.findById(NOT_EXIST_USER_ID))
                .thenReturn(Optional.empty());
    }
}
