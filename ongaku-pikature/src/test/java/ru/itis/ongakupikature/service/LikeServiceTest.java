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
import ru.itis.ongakupikature.entity.PlaylistMusic;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.PlaylistMusicRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Epic("Песня")
@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    private static final Long FAVORITE_PLAYLIST_ID = 1L;
    private static final Long MUSIC_ID = 2L;
    private static final PlaylistMusic PLAYLIST_MUSIC = PlaylistMusic.builder()
            .id(1L)
            .playlistId(FAVORITE_PLAYLIST_ID)
            .musicId(MUSIC_ID)
            .build();
    private static final User USER = User.builder()
            .favoritePlaylistId(FAVORITE_PLAYLIST_ID)
            .build();

    @Mock
    private PlaylistMusicRepository playlistMusicRepository;

    @InjectMocks
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(playlistMusicRepository.save(any()))
                .thenReturn(new PlaylistMusic());
        Mockito.lenient().doNothing()
                .when(playlistMusicRepository)
                .delete(any());
        Mockito.lenient().when(playlistMusicRepository.existsByPlaylistIdAndMusicId(FAVORITE_PLAYLIST_ID, MUSIC_ID))
                .thenReturn(true);
        Mockito.lenient().when(playlistMusicRepository.findByPlaylistIdAndMusicId(FAVORITE_PLAYLIST_ID, MUSIC_ID))
                .thenReturn(PLAYLIST_MUSIC);
    }

    @Test
    @DisplayName("Поставить лайк")
    @Feature("Лайк")
    @Story("Сервис")
    void addLike_shouldSaveSongToFavoritePlaylist() {
        likeService.addLike(USER, MUSIC_ID);
        verifySaveSongToFavoritePlaylist(playlistMusicRepository);
    }

    @Test
    @DisplayName("Убрать лайк")
    @Feature("Лайк")
    @Story("Сервис")
    void deleteLike_shouldDeleteSongFromFavoritePlaylist() {
        likeService.deleteLike(USER, MUSIC_ID);
        verifyDeleteSongFromFavoritePlaylist(playlistMusicRepository);
    }

    @Test
    @DisplayName("Проверить, что лайк поставлен")
    @Feature("Лайк")
    @Story("Сервис")
    void isLiked_shouldReturnTrue() {
        var isLiked = likeService.isLiked(USER, MUSIC_ID);
        verifyExistsByPlaylistIdAndMusicId(playlistMusicRepository, MUSIC_ID);
        checkIsLikedTrue(isLiked);
    }

    @Test
    @DisplayName("Проверить, что лайк не поставлен")
    @Feature("Лайк")
    @Story("Сервис")
    void isLiked_shouldReturnFalse() {
        var notLikedMusicId = MUSIC_ID + 1;
        var isLiked = likeService.isLiked(USER, notLikedMusicId);
        verifyExistsByPlaylistIdAndMusicId(playlistMusicRepository, notLikedMusicId);
        checkIsLikedFalse(isLiked);
    }

    @Step("Сохранение песни в плейлист \"Избранное\"")
    static void verifySaveSongToFavoritePlaylist(PlaylistMusicRepository playlistMusicRepository) {
        verify(playlistMusicRepository)
                .save(PlaylistMusic.builder()
                        .playlistId(FAVORITE_PLAYLIST_ID)
                        .musicId(MUSIC_ID)
                        .build()
                );
    }

    @Step("Удаление песни из плейлиста \"Избранное\"")
    static void verifyDeleteSongFromFavoritePlaylist(PlaylistMusicRepository playlistMusicRepository) {
        verify(playlistMusicRepository)
                .delete(PLAYLIST_MUSIC);
    }

    @Step("Лайк поставлен")
    static void checkIsLikedTrue(boolean isLiked) {
        assertThat(isLiked)
                .isTrue();
    }

    @Step("Лайк не поставлен")
    static void checkIsLikedFalse(boolean isLiked) {
        assertThat(isLiked)
                .isFalse();
    }

    @Step("Поиск песни в плейлисте \"Избранное\"")
    static void verifyExistsByPlaylistIdAndMusicId(PlaylistMusicRepository playlistMusicRepository,
                                                   Long musicId) {
        verify(playlistMusicRepository)
                .existsByPlaylistIdAndMusicId(FAVORITE_PLAYLIST_ID, musicId);
    }
}
