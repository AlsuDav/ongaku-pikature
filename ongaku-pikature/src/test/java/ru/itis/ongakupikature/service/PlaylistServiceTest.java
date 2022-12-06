package ru.itis.ongakupikature.service;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.entity.Music;
import ru.itis.ongakupikature.entity.Playlist;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.MusicRepository;
import ru.itis.ongakupikature.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Epic("Плейлист")
@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    private static final Long USER_ID = 1L;
    private static final User USER = User.builder()
            .id(USER_ID)
            .build();

    private static final Long PLAYLIST_ID = 100L;
    private static final Long MUSIC_ID = 5L;
    private static final Long NEW_MUSIC_ID = 6L;
    private static final Music MUSIC = Music.builder()
            .id(MUSIC_ID)
            .build();

    private static final Music NEW_MUSIC = Music.builder()
            .id(NEW_MUSIC_ID)
            .build();

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private PlaylistService playlistService;

    @DisplayName("Удалить песню из плейлиста другого пользователя")
    @Feature("Удалить песню из плейлиста")
    @Story("Метод")
    @Test
    void deleteSongFromPlaylist_notUserPlaylist() {
        given(playlistRepository.findByIdAndUserId(any(), any())).willReturn(null);

        var result = playlistService.deleteSongFromPlaylist(USER, PLAYLIST_ID, MUSIC_ID);

        verify(playlistRepository).findByIdAndUserId(PLAYLIST_ID, USER_ID);
        assertThat(result)
                .isInstanceOf(ActionResult.Error.class);
    }

    @DisplayName("Удалить песню, которой нет в плейлисте")
    @Feature("Удалить песню из плейлиста")
    @Story("Метод")
    @Test
    void deleteSongFromPlaylist_songNotBelongToPlaylist() {
        var playlist = Playlist.builder()
                .id(PLAYLIST_ID)
                .musicList(new ArrayList<>(List.of(MUSIC)))
                .build();
        given(playlistRepository.findByIdAndUserId(any(), any())).willReturn(playlist);

        var result = playlistService.deleteSongFromPlaylist(USER, PLAYLIST_ID, 4L);

        verify(playlistRepository).findByIdAndUserId(PLAYLIST_ID, USER_ID);
        assertThat(result)
                .isInstanceOf(ActionResult.Error.class);
    }

    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное удаление песни из плейлиста")
    @Feature("Удалить песню из плейлиста")
    @Story("Метод")
    @Test
    void deleteSongFromPlaylist_success() {
        var playlist = Playlist.builder()
                .id(PLAYLIST_ID)
                .musicList(new ArrayList<>(List.of(MUSIC)))
                .build();
        given(playlistRepository.findByIdAndUserId(any(), any())).willReturn(playlist);

        var result = playlistService.deleteSongFromPlaylist(USER, PLAYLIST_ID, MUSIC_ID);

        verify(playlistRepository).findByIdAndUserId(PLAYLIST_ID, USER_ID);
        var playlistAfterDelete = Playlist.builder()
                .id(PLAYLIST_ID)
                .musicList(Collections.emptyList())
                .build();
        verify(playlistRepository).save(playlistAfterDelete);
        assertThat(result)
                .isInstanceOf(ActionResult.Success.class);
    }

    @DisplayName("Добавить песню в плейлист другого пользователя")
    @Feature("Добавить песню в плейлист")
    @Story("Метод")
    @Test
    void addSongToPlaylist_notUserPlaylist() {
        given(playlistRepository.findByIdAndUserId(any(), any())).willReturn(null);

        var result = playlistService.addSongToPlaylist(USER, PLAYLIST_ID, MUSIC_ID);

        verify(playlistRepository).findByIdAndUserId(PLAYLIST_ID, USER_ID);
        assertThat(result)
                .isInstanceOf(ActionResult.Error.class);
    }

    @DisplayName("Добавить несуществующую песню в плейлист")
    @Feature("Добавить песню в плейлист")
    @Story("Метод")
    @Test
    void addSongToPlaylist_musicNotExists() {
        var playlist = Playlist.builder()
                .id(PLAYLIST_ID)
                .musicList(new ArrayList<>(List.of(MUSIC)))
                .build();
        given(playlistRepository.findByIdAndUserId(any(), any())).willReturn(playlist);
        given(musicRepository.findById(any())).willReturn(Optional.empty());

        var result = playlistService.addSongToPlaylist(USER, PLAYLIST_ID, NEW_MUSIC_ID);

        verify(playlistRepository).findByIdAndUserId(PLAYLIST_ID, USER_ID);
        verify(musicRepository).findById(NEW_MUSIC_ID);
        assertThat(result)
                .isInstanceOf(ActionResult.Error.class);
    }

    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное добавление песни в плейлист")
    @Feature("Добавить песню в плейлист")
    @Story("Метод")
    @Test
    void addSongToPlaylist_success() {
        var playlist = Playlist.builder()
                .id(PLAYLIST_ID)
                .musicList(new ArrayList<>(List.of(MUSIC)))
                .build();
        given(playlistRepository.findByIdAndUserId(any(), any())).willReturn(playlist);
        given(musicRepository.findById(any())).willReturn(Optional.of(NEW_MUSIC));

        var result = playlistService.addSongToPlaylist(USER, PLAYLIST_ID, NEW_MUSIC_ID);

        verify(playlistRepository).findByIdAndUserId(PLAYLIST_ID, USER_ID);
        verify(musicRepository).findById(NEW_MUSIC_ID);
        var playlistAfterAdd = Playlist.builder()
                .id(PLAYLIST_ID)
                .musicList(new ArrayList<>(List.of(MUSIC, NEW_MUSIC)))
                .build();
        verify(playlistRepository).save(playlistAfterAdd);
        assertThat(result)
                .isInstanceOf(ActionResult.Success.class);
    }
}
