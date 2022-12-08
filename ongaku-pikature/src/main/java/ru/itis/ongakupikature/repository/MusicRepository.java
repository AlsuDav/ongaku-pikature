package ru.itis.ongakupikature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.Music;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
