package ru.itis.ongakupikature.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.entity.Author;
import ru.itis.ongakupikature.entity.Music;
import ru.itis.ongakupikature.repository.MusicRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;

    public List<MusicDto> getMainPageMusic() {
        var allMusicPage = musicRepository.findAll(PageRequest.of(0, 10, Sort.by("id")));
        return allMusicPage.getContent().stream()
                .map(this::toDto)
                .toList();
    }

    //TODO finish
    private MusicDto toDto(Music music) {
        var authors = music.getAuthors().stream()
                .map(Author::getName)
                .toList();
        return MusicDto.builder()
                .id(music.getId())
                .name(music.getName())
                .musicPath("")
                .picturePath("")
                .authors(authors)
                .build();
    }
}