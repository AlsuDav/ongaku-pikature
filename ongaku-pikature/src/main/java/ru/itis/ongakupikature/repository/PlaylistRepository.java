package ru.itis.ongakupikature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Playlist findByIdAndUserId(Long id, Long userId);
}
