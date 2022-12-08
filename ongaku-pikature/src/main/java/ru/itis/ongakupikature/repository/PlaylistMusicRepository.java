package ru.itis.ongakupikature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.PlaylistMusic;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, Long> {

    PlaylistMusic findByPlaylistIdAndMusicId(Long playlistId, Long musicId);

    boolean existsByPlaylistIdAndMusicId(Long playlistId, Long musicId);
}
