package ru.itis.ongakupikature.service;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.NeuroImageComment;
import ru.itis.ongakupikature.entity.Music;
import ru.itis.ongakupikature.entity.NeuroText;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.filestorage.FileStorage;
import ru.itis.ongakupikature.filestorage.dto.FileUuid;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.repository.MusicRepository;
import ru.itis.ongakupikature.repository.NeuroTextRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Epic("Генерация изображения")
@ExtendWith(MockitoExtension.class)
class GenerateImageServiceTest {

    private static final User USER = User.builder().build();
    private static final Long FIRST_MUSIC_ID = 1L;
    private static final Long SECOND_MUSIC_ID = 2L;
    private static final NeuroText NEURO_TEXT = NeuroText.builder()
            .userKeyWords("userKeyWords")
            .build();
    private static final Music MUSIC = Music.builder()
            .autoKeyWords("autoKeyWords")
            .build();
    private static final MusicDto FIRST_MUSIC = MusicDto.builder()
            .id(FIRST_MUSIC_ID)
            .build();
    private static final String IMAGE_PATH = "/path/image.png";
    private static final FileUuid FILE_UUID = new FileUuid("image.png", "/path/");

    @Mock
    private NeuroTextRepository neuroTextRepository;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private GenerateImageService generateImageService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(neuroTextRepository.findByMusicIdAndUser(FIRST_MUSIC_ID, USER))
                .thenReturn(null);
        Mockito.lenient().when(neuroTextRepository.findByMusicIdAndUser(SECOND_MUSIC_ID, USER))
                .thenReturn(NEURO_TEXT);
        Mockito.lenient().when(musicRepository.findById(any()))
                .thenReturn(Optional.of(MUSIC));
    }

    @Test
    @DisplayName("Ошибка загрузки изображения после первой генерации")
    @Feature("Генерация изображения")
    @Story("Метод")
    void generateImage_firstTimeWithDefaultPath() {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Failed.FileNotLoaded(""));
        var imagePath = generateImageService.generateImage(FIRST_MUSIC, USER);

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, FIRST_MUSIC_ID);
        verifyMusicFindById(musicRepository, FIRST_MUSIC_ID);
        verifyImageLoadToStorage(fileStorage);
        checkImagePathIsDefault(imagePath);
    }

    @Test
    @DisplayName("Успешная первая генерация изображения")
    @Feature("Генерация изображения")
    @Story("Метод")
    void generateImage_firstTimeWithImagePath() {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Success(FILE_UUID));
        var imagePath = generateImageService.generateImage(FIRST_MUSIC, USER);

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, FIRST_MUSIC_ID);
        verifyMusicFindById(musicRepository, FIRST_MUSIC_ID);
        verifyImageLoadToStorage(fileStorage);
        verifyNeuroTextSave(neuroTextRepository);
        checkImagePathIsNotDefault(imagePath);
    }

    @Test
    @DisplayName("Ошибка загрузки изображения после повторной генерации")
    @Feature("Генерация изображения")
    @Story("Метод")
    void generateImage_secondTimeWithDefaultPath() {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Failed.FileNotLoaded(""));
        var imagePath = generateImageService.generateImage(
                MusicDto.builder()
                        .id(SECOND_MUSIC_ID)
                        .build(),
                USER
        );

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, SECOND_MUSIC_ID);
        verifyMusicFindById(musicRepository, SECOND_MUSIC_ID);
        verifyImageLoadToStorage(fileStorage);
        checkImagePathIsDefault(imagePath);
    }

    @Test
    @DisplayName("Успешная повторная генерация")
    @Feature("Генерация изображения")
    @Story("Метод")
    void generateImage_secondTimeWithNotDefaultPath() {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Success(FILE_UUID));
        var imagePath = generateImageService.generateImage(
                MusicDto.builder()
                        .id(SECOND_MUSIC_ID)
                        .build(),
                USER
        );

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, SECOND_MUSIC_ID);
        verifyMusicFindById(musicRepository, SECOND_MUSIC_ID);
        verifyImageLoadToStorage(fileStorage);
        verifyNeuroTextSave(neuroTextRepository);
        checkImagePathIsNotDefault(imagePath);
    }

    @Test
    @DisplayName("Ошибка загрузки изображения после добавления комментария")
    @Feature("Добавление комментария к изображению")
    @Story("Метод")
    void addComment_loadImageError() {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Failed.FileNotLoaded(""));

        var imagePath = generateImageService.addComment(
                new NeuroImageComment(SECOND_MUSIC_ID, "comment"),
                USER
        );

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, SECOND_MUSIC_ID);
        verifyNeuroTextSaveComment(neuroTextRepository);
        verifyImageLoadToStorage(fileStorage);
        checkImagePathIsDefault(imagePath);
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    @Feature("Добавление комментария к изображению")
    @Story("Метод")
    void addComment_success() {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Success(FILE_UUID));

        var imagePath = generateImageService.addComment(
                new NeuroImageComment(SECOND_MUSIC_ID, "comment"),
                USER
        );

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, SECOND_MUSIC_ID);
        verifyNeuroTextSaveImageAndComment(neuroTextRepository);
        verifyImageLoadToStorage(fileStorage);
        checkImagePathIsNotDefault(imagePath);
    }

    @Step("Получен дефолтный путь до изображения")
    private static void checkImagePathIsDefault(String imagePath) {
        assertThat(imagePath)
                .isNotEqualTo(IMAGE_PATH);
    }

    @Step("Получен сохраненного путь до изображения")
    private static void checkImagePathIsNotDefault(String imagePath) {
        assertThat(imagePath)
                .isEqualTo(IMAGE_PATH);
    }

    @Step("Поиск уже сгенерированного изображения для пользователя")
    private static void verifyNeuroTextFindByMusicIdAndUser(NeuroTextRepository neuroTextRepository,
                                                            Long musicId) {
        verify(neuroTextRepository)
                .findByMusicIdAndUser(musicId, USER);
    }

    @Step("Поиск песни")
    private static void verifyMusicFindById(MusicRepository musicRepository,
                                            Long musicId) {
        verify(musicRepository)
                .findById(musicId);
    }

    @Step("Загрузка изображения")
    private static void verifyImageLoadToStorage(FileStorage fileStorage) {
        verify(fileStorage)
                .loadFileToStorage(any());
    }

    @Step("Сохранение изображения")
    private static void verifyNeuroTextSave(NeuroTextRepository neuroTextRepository) {
        verify(neuroTextRepository)
                .save(any());
    }

    @Step("Сохранение изображения и комментария")
    private static void verifyNeuroTextSaveImageAndComment(NeuroTextRepository neuroTextRepository) {
        verify(neuroTextRepository, times(2))
                .save(any());
    }

    @Step("Сохранение комментария")
    private static void verifyNeuroTextSaveComment(NeuroTextRepository neuroTextRepository) {
        verify(neuroTextRepository)
                .save(any());
    }
}
