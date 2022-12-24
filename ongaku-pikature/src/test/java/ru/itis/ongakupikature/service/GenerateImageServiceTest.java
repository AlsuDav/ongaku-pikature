package ru.itis.ongakupikature.service;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.itis.ongakupikature.api.ImageGeneration;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private static final String IMAGE_PATH = "src/test/resources/notDefaultSongPoster.jpg";
    private static final FileUuid FILE_UUID = new FileUuid("notDefaultSongPoster.jpg", "src/test/resources/");
    private static final String SRC_PATH = "src/test/resources/";
    @Mock
    private NeuroTextRepository neuroTextRepository;

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private ImageGeneration imageGeneration;

    @InjectMocks
    private GenerateImageService generateImageService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(generateImageService, "defaultImagePath", "src/test/resources/defaultSongPoster.png");
        Mockito.lenient().when(imageGeneration.generateImageByKeyWords(any()))
                .thenReturn(InputStream.nullInputStream());
        Mockito.lenient().when(neuroTextRepository.findByUserAndMusicId(USER, FIRST_MUSIC_ID))
                .thenReturn(null);
        Mockito.lenient().when(neuroTextRepository.findByUserAndMusicId(USER, SECOND_MUSIC_ID))
                .thenReturn(NEURO_TEXT);
        Mockito.lenient().when(musicRepository.findById(any()))
                .thenReturn(Optional.of(MUSIC));
    }

    @Test
    @DisplayName("Ошибка загрузки изображения после первой генерации")
    @Feature("Генерация изображения")
    @Story("Метод")
    void generateImage_firstTimeWithDefaultPath() throws IOException {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Failed.FileNotLoaded(""));
        var imagePath = generateImageService.generateImage(FIRST_MUSIC.id(), USER);

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, FIRST_MUSIC_ID);
        verifyMusicFindById(musicRepository, FIRST_MUSIC_ID);
        verifyImageLoadToStorage(fileStorage);
        checkImagePathIsDefault(imagePath);
    }

    @Test
    @DisplayName("Успешная первая генерация изображения")
    @Feature("Генерация изображения")
    @Story("Метод")
    void generateImage_firstTimeWithImagePath() throws IOException {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Success(FILE_UUID));
        var imagePath = SRC_PATH + generateImageService.generateImage(FIRST_MUSIC.id(), USER);

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
    void generateImage_secondTimeWithDefaultPath() throws IOException {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Failed.FileNotLoaded(""));
        var imagePath = generateImageService.generateImage(

                SECOND_MUSIC_ID,

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
    void generateImage_secondTimeWithNotDefaultPath() throws IOException {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Success(FILE_UUID));
        var imagePath = SRC_PATH + generateImageService.generateImage(
                SECOND_MUSIC_ID,
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
    void addComment_loadImageError() throws IOException {
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
    void addComment_success() throws IOException {
        given(fileStorage.loadFileToStorage(any()))
                .willReturn(new LoadResult.Success(FILE_UUID));

        var imagePath = SRC_PATH + generateImageService.addComment(
                new NeuroImageComment(SECOND_MUSIC_ID, "comment"),
                USER
        );

        verifyNeuroTextFindByMusicIdAndUser(neuroTextRepository, SECOND_MUSIC_ID);
        verifyNeuroTextSaveImageAndComment(neuroTextRepository);
        verifyImageLoadToStorage(fileStorage);
        checkImagePathIsNotDefault(imagePath);
    }

    @Step("Получен дефолтный путь до изображения")
    private static void checkImagePathIsDefault(String imagePath) throws IOException {
        assertThat(imagePath)
                .isNotEqualTo(IMAGE_PATH);
        getBytes(imagePath);
    }

    @Step("Получен сохраненного путь до изображения")
    private static void checkImagePathIsNotDefault(String imagePath) throws IOException {
        assertThat(imagePath)
                .isEqualTo(IMAGE_PATH);
        getBytes(imagePath);
    }

    @Step("Поиск уже сгенерированного изображения для пользователя")
    private static void verifyNeuroTextFindByMusicIdAndUser(NeuroTextRepository neuroTextRepository,
                                                            Long musicId) {
        verify(neuroTextRepository)
                .findByUserAndMusicId(USER, musicId);
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

    @Attachment(value = "Полученное изображение", type = "image/jpeg", fileExtension = ".jpg")
    public static byte[] getBytes(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(filename));
    }
}
