package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.ongakupikature.entity.PlaylistMusic;
import ru.itis.ongakupikature.repository.PlaylistMusicRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PlaylistMusicRepository playlistMusicRepository;

    @Transactional
    public void addLike(Long playlistId, Long musicId) {
        var playlistMusic = PlaylistMusic.builder()
                .playlistId(playlistId)
                .musicId(musicId)
                .build();
        playlistMusicRepository.save(playlistMusic);
    }

    @Transactional
    public void deleteLike(Long playlistId, Long musicId) {
        var playlistMusic = playlistMusicRepository.findByPlaylistIdAndMusicId(playlistId, musicId);
        playlistMusicRepository.delete(playlistMusic);
    }
}
