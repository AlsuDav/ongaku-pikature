package ru.itis.ongakupikature.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.ongakupikature.entity.Music;

public interface MusicRepository extends JpaRepository<Music, Long> {

    @Override
    @NonNull
    @EntityGraph(value = "music.authors")
    Page<Music> findAll(@NonNull Pageable pageable);
}
