package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.dto.PlaylistDto;
import ru.itis.ongakupikature.entity.Playlist;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.MusicRepository;
import ru.itis.ongakupikature.repository.PlaylistRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;

    public PlaylistDto getPlaylistById(Long id) {
        var playlist = playlistRepository.findById(id);
        return playlist.map(this::toDto).orElse(null);
    }

    public List<PlaylistDto> getUserPlaylists(User user) {
        var playlists = playlistRepository.findAllByUser(user);
        return playlists.stream()
                .map(this::toDto)
                .toList();
    }

    public ActionResult createUserPlaylist(User user, String name) {
        try {
            var playlist = Playlist.builder()
                    .name(name)
                    .user(user)
                    .dateCreate(LocalDate.now())
                    .build();
            playlistRepository.save(playlist);
            return new ActionResult.Success();
        } catch (Exception e) {
            return new ActionResult.Error();
        }
    }

    public ActionResult deleteUserPlaylist(User user, Long playlistId) {
        try {
            playlistRepository.deleteByUserAndId(user, playlistId);
            return new ActionResult.Success();
        } catch (Exception e) {
            return new ActionResult.Error();
        }
    }

    private PlaylistDto toDto(Playlist playlist) {
        return PlaylistDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .songCount(playlist.getMusicList().size())
                .build();
    }

    public ActionResult deleteSongFromPlaylist(User user, Long playlistId, Long musicId) {
        var playlist = playlistRepository.findByIdAndUserId(playlistId, user.getId());
        if (playlist == null) {
            return new ActionResult.Error();
        }
        var optionalMusic = playlist.getMusicList().stream().filter(m -> Objects.equals(m.getId(), musicId)).findFirst();
        if (optionalMusic.isEmpty()) {
            return new ActionResult.Error();
        }
        playlist.getMusicList().remove(optionalMusic.get());
        playlistRepository.save(playlist);
        return new ActionResult.Success();
    }

    public ActionResult addSongToPlaylist(User user, Long playlistId, Long musicId) {
        var playlist = playlistRepository.findByIdAndUserId(playlistId, user.getId());
        if (playlist == null) {
            return new ActionResult.Error();
        }
        var optionalMusic = musicRepository.findById(musicId);
        if (optionalMusic.isEmpty()) {
            return new ActionResult.Error();
        }
        playlist.getMusicList().add(optionalMusic.get());
        playlistRepository.save(playlist);
        return new ActionResult.Success();
    }
}
