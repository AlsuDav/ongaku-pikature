package ru.itis.ongakupikature.service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.dto.PlaylistDto;
import ru.itis.ongakupikature.entity.Music;
import ru.itis.ongakupikature.entity.Playlist;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Epic("Плейлист")
@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    private static final User USER = User.builder().build();

    private static final List<PlaylistDto> PLAYLIST_DTO_LIST = List.of(
            PlaylistDto.builder()
                    .id(1L)
                    .name("Избранное")
                    .songCount(0)
                    .build(),
            PlaylistDto.builder()
                    .id(2L)
                    .name("Плейлист 1")
                    .songCount(2)
                    .build()
    );

    private static final List<Playlist> PLAYLIST_LIST = List.of(
            Playlist.builder()
                    .id(1L)
                    .name("Избранное")
                    .musicList(new ArrayList<>())
                    .build(),
            Playlist.builder()
                    .id(2L)
                    .name("Плейлист 1")
                    .musicList(
                            List.of(
                                    Music.builder()
                                            .build(),
                                    Music.builder()
                                            .build()
                            )
                    )
                    .build()
    );

    private static final List<PlaylistDto> ONLY_FAVORITE_PLAYLIST_DTO_LIST = List.of(
            PlaylistDto.builder()
                    .id(1L)
                    .name("Избранное")
                    .songCount(0)
                    .build()
    );

    private static final List<Playlist> ONLY_FAVORITE_PLAYLIST_LIST = List.of(
            Playlist.builder()
                    .id(1L)
                    .name("Избранное")
                    .musicList(new ArrayList<>())
                    .build()
    );

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private PlaylistService playlistService;

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("У пользователя есть плейлисты")
    @Feature("Получить список плейлистов пользователя")
    @Story("Метод")
    void getUserPlaylists_userHasPlaylists() {
        given(playlistRepository.findAllByUser(any())).willReturn(PLAYLIST_LIST);

        var musicList = playlistService.getUserPlaylists(USER);

        checkUserHasPlaylists(musicList);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Пользователь не создавал плейлисты")
    @Feature("Получить список плейлистов пользователя")
    @Story("Метод")
    void getUserPlaylists_userHasNotPlaylists() {
        given(playlistRepository.findAllByUser(any())).willReturn(ONLY_FAVORITE_PLAYLIST_LIST);

        var musicList = playlistService.getUserPlaylists(USER);

        checkUserHasNotPlaylists(musicList);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное удаление плейлиста")
    @Feature("Удалить плейлист")
    @Story("Метод")
    void deletePlaylist_success() {
        Mockito.lenient().doNothing().when(playlistRepository).deleteByUserAndId(any(), any());

        var result = playlistService.deleteUserPlaylist(USER, 1L);

        checkActionResultSuccess(result);
    }

    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Неуспешное удаление плейлиста")
    @Feature("Удалить плейлист")
    @Story("Метод")
    void deletePlaylist_error() {
        Mockito.lenient().doThrow(new RuntimeException()).when(playlistRepository).deleteByUserAndId(any(), any());

        var result = playlistService.deleteUserPlaylist(USER, 1L);

        checkActionResultError(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное создание плейлиста")
    @Feature("Создать плейлист")
    @Story("Метод")
    void createPlaylist_success() {
        given(playlistRepository.save(any())).willReturn(new Playlist());

        var result = playlistService.createUserPlaylist(USER, "New playlist");

        checkActionResultSuccess(result);
    }

    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Неуспешное создание плейлиста")
    @Feature("Создать плейлист")
    @Story("Метод")
    void createPlaylist_error() {
        Mockito.lenient().doThrow(new RuntimeException()).when(playlistRepository).save(any());

        var result = playlistService.createUserPlaylist(USER, "New playlist");

        checkActionResultError(result);
    }

    @Step("У пользователя два плейлиста: \"Избранное\" и \"Плейлист 1\"")
    private void checkUserHasPlaylists(List<PlaylistDto> playlistDtoList) {
        assertThat(playlistDtoList)
                .containsExactlyInAnyOrderElementsOf(PLAYLIST_DTO_LIST);
    }

    @Step("У пользователя один плейлист по умолчанию: \"Избранное\"")
    private void checkUserHasNotPlaylists(List<PlaylistDto> playlistDtoList) {
        assertThat(playlistDtoList)
                .containsExactlyInAnyOrderElementsOf(ONLY_FAVORITE_PLAYLIST_DTO_LIST);
    }

    @Step("Успешный ответ")
    private void checkActionResultSuccess(ActionResult result) {
        assertThat(result)
                .isInstanceOf(ActionResult.Success.class);
    }

    @Step("Неуспешный ответ")
    private void checkActionResultError(ActionResult result) {
        assertThat(result)
                .isInstanceOf(ActionResult.Error.class);
    }
}
