package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.MusicRepository;
import ru.itis.ongakupikature.repository.PlaylistRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;

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
