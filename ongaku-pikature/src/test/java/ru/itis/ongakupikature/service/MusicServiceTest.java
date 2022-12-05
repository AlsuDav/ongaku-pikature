package ru.itis.ongakupikature.service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.MusicMoreData;
import ru.itis.ongakupikature.entity.*;
import ru.itis.ongakupikature.repository.MusicRepository;
import ru.itis.ongakupikature.repository.NeuroTextRepository;
import ru.itis.ongakupikature.repository.PlaylistRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Epic("Песня")
@ExtendWith(MockitoExtension.class)
class MusicServiceTest {

    private static final Long MUSIC_ID = 1L;

    private static final MusicMoreData MUSIC_MORE_DATA =
            new MusicMoreData(true, "path");

    private static final List<Music> MUSIC_LIST = List.of(
            Music.builder()
                    .id(MUSIC_ID)
                    .name("name")
                    .authors(List.of(
                            Author.builder()
                                    .name("author1")
                                    .build(),
                            Author.builder()
                                    .name("author2").build())
                    )
                    .filePath("musicPath")
                    .posterPath("posterPath")
                    .build(),
            Music.builder()
                    .id(2L)
                    .name("name2")
                    .authors(List.of(Author.builder()
                            .name("author1")
                            .build()))
                    .filePath("musicPath")
                    .posterPath("posterPath")
                    .build()
    );

    private static final List<MusicDto> MUSIC_DTO_LIST = List.of(
            MusicDto.builder()
                    .id(MUSIC_ID)
                    .name("name")
                    .authors(List.of("author1", "author2"))
                    .musicPath("musicPath")
                    .posterPath("posterPath")
                    .build(),
            MusicDto.builder()
                    .id(2L)
                    .name("name2")
                    .authors(List.of("author1"))
                    .musicPath("musicPath")
                    .posterPath("posterPath")
                    .build()
    );

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private LikeService likeService;

    @Mock
    private NeuroTextRepository neuroTextRepository;

    @InjectMocks
    private MusicService musicService;

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Получить песни для главной страницы")
    @Feature("Все песни")
    @Story("Метод")
    void getAllMusic_shouldReturnAllMusic() {
        given(musicRepository.findAll((Pageable) any())).willReturn( new PageImpl<>(MUSIC_LIST));

        var musicList = musicService.getAllMusic();

        checkMusicDtoList(musicList);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Получить песни плейлиста")
    @Feature("Песни плейлиста")
    @Story("Метод")
    void getPlaylistMusic_shouldReturnAllPlaylistMusic() {
        given(playlistRepository.findById(MUSIC_ID)).willReturn(Optional.of(
                Playlist.builder()
                        .musicList(MUSIC_LIST)
                        .build()));

        var musicList = musicService.getPlaylistMusic(MUSIC_ID);

        checkMusicDtoList(musicList);
    }

    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Ошибка при получении песен")
    @Feature("Песни плейлиста")
    @Story("Метод")
    void getPlaylistMusic_shouldThrowError() {
        given(playlistRepository.findById(1L))
                .willThrow(new RuntimeException());

        checkExceptionThrow(() -> musicService.getPlaylistMusic(1L));
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешно поставить лайк")
    @Feature("Лайк")
    @Story("Метод")
    void setLike_shouldAddLike() {
        Mockito.lenient().doNothing().when(likeService).addLike(any(), any());

        var result = musicService.setLike(User.builder().build(), MUSIC_ID, true);

        checkLikeDeleted(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешно убрать лайк")
    @Feature("Лайк")
    @Story("Метод")
    void setLike_shouldDeleteLike() {
        Mockito.lenient().doNothing().when(likeService). deleteLike(any(), any());

        var result = musicService.setLike(User.builder().build(), MUSIC_ID, false);

        checkLikeAdded(result);
    }

    @ParameterizedTest(name = "{displayName}[{index}] {arguments}")
    @DisplayName("Ошибка при обработке лайка")
    @Feature("Лайк")
    @Story("Метод")
    @ValueSource(booleans = {false, true})
    void setLike_error(boolean isLike) {
        Mockito.lenient().doThrow(new RuntimeException()).when(likeService).deleteLike(any(), any());
        Mockito.lenient().doThrow(new RuntimeException()).when(likeService).addLike(any(), any());

        var result = musicService.setLike(User.builder().build(), MUSIC_ID, isLike);

        checkLikeError(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Получить дополнительную информацию по песне")
    @Feature("Дополнительная информация")
    @Story("Метод")
    void getMusicMoreData_shouldReturnOk() {
        var user = User.builder().build();
        given(neuroTextRepository.findByUserAndMusicId(user, MUSIC_ID))
                .willReturn(NeuroText.builder()
                        .userPicturePath("path")
                        .build());
        given(likeService.isLiked(user, MUSIC_ID))
                .willReturn(true);

        var musicData = musicService.getMusicData(user, MUSIC_ID);

        checkMusicMoreDto(musicData);
    }

    @Step("Лист песен совпадают с ожидаемым")
    private static void checkMusicDtoList(List<MusicDto> musicDtoList) {
        assertThat(musicDtoList)
                .containsExactlyInAnyOrderElementsOf(MUSIC_DTO_LIST);
    }

    @Step("Произошла ошибка")
    private static void checkExceptionThrow(Executable executable) {
        assertThrows(RuntimeException.class,
                executable);
    }

    @Step("Лайк поставлен")
    private static void checkLikeAdded(boolean result) {
        assertThat(result)
                .isTrue();
    }

    @Step("Лайк удален")
    private static void checkLikeDeleted(boolean result) {
        assertThat(result)
                .isTrue();
    }

    @Step("Неуспешная обработка лайка")
    private static void checkLikeError(boolean result) {
        assertThat(result)
                .isFalse();
    }

    @Step("Дополнительная информация по песне совпадает с ожидаемой")
    private void checkMusicMoreDto(MusicMoreData musicMoreData) {
        assertThat(musicMoreData)
                .isEqualTo(MUSIC_MORE_DATA);
    }
}
