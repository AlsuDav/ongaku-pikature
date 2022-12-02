package ru.itis.ongakupikature.service;

import org.junit.jupiter.api.BeforeEach;
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
    void addLike_shouldSaveSongToFavoritePlaylist() {
        likeService.addLike(USER, MUSIC_ID);
        verify(playlistMusicRepository)
                .save(PlaylistMusic.builder()
                        .playlistId(FAVORITE_PLAYLIST_ID)
                        .musicId(MUSIC_ID)
                        .build()
                );
    }

    @Test
    void deleteLike_shouldDeleteSongFromFavoritePlaylist() {
        likeService.deleteLike(USER, MUSIC_ID);
        verify(playlistMusicRepository)
                .delete(PLAYLIST_MUSIC);
    }

    @Test
    void isLiked_shouldReturnTrue() {
        var isLiked = likeService.isLiked(USER, MUSIC_ID);
        assertThat(isLiked)
                .isTrue();
        verify(playlistMusicRepository)
                .existsByPlaylistIdAndMusicId(FAVORITE_PLAYLIST_ID, MUSIC_ID);
    }

    @Test
    void isLiked_shouldReturnFalse() {
        var notLikedMusicId = MUSIC_ID + 1;
        var isLiked = likeService.isLiked(USER, notLikedMusicId);
        assertThat(isLiked)
                .isFalse();
        verify(playlistMusicRepository)
                .existsByPlaylistIdAndMusicId(FAVORITE_PLAYLIST_ID, notLikedMusicId);
    }
}
