package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.MusicMoreData;
import ru.itis.ongakupikature.entity.Author;
import ru.itis.ongakupikature.entity.Music;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.MusicRepository;
import ru.itis.ongakupikature.repository.NeuroTextRepository;
import ru.itis.ongakupikature.repository.PlaylistRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final PlaylistRepository playlistRepository;
    private final LikeService likeService;
    private final NeuroTextRepository neuroTextRepository;

    public List<MusicDto> getAllMusic() {
        var allMusicPage = musicRepository.findAll(PageRequest.of(0, 10, Sort.by("id")));
        return allMusicPage.getContent().stream()
                .map(this::toDto)
                .toList();
    }

    public List<MusicDto> getPlaylistMusic(Long playlistId) {
        var playlist = playlistRepository.findById(playlistId).orElseThrow();
        return playlist.getMusicList().stream()
                .map(this::toDto)
                .toList();
    }

    public boolean setLike(User user, Long musicId, boolean isLike) {
        try {
            if (isLike) {
                likeService.addLike(user, musicId);
            } else {
                likeService.deleteLike(user, musicId);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public MusicMoreData getMusicData(User user, Long musicId) {
        var neuroText = neuroTextRepository.findByUserAndMusicId(user, musicId);
        var isLiked = likeService.isLiked(user, musicId);
        return new MusicMoreData(isLiked, neuroText.getUserPicturePath());
    }

    private MusicDto toDto(Music music) {
        var authors = music.getAuthors().stream()
                .map(Author::getName)
                .toList();
        return MusicDto.builder()
                .id(music.getId())
                .name(music.getName())
                .musicPath(music.getFilePath())
                .posterPath(music.getPosterPath())
                .authors(authors)
                .build();
    }
}
