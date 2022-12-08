package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.NeuroImageComment;
import ru.itis.ongakupikature.entity.Music;
import ru.itis.ongakupikature.entity.NeuroText;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.filestorage.FileStorage;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.filestorage.dto.UploadParams;
import ru.itis.ongakupikature.repository.MusicRepository;
import ru.itis.ongakupikature.repository.NeuroTextRepository;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class GenerateImageService {

    private static final String DEFAULT_IMAGE_PATH = "";

    private final NeuroTextRepository neuroTextRepository;
    private final MusicRepository musicRepository;
    private final FileStorage fileStorage;

    public String generateImage(MusicDto musicDto, User user) {
        var neuroText = neuroTextRepository.findByMusicIdAndUser(musicDto.id(), user);
        var music = musicRepository.findById(musicDto.id()).orElseThrow();

        if (neuroText == null) {
            return generateImageFirstTime(music, user);
        }
        return generateImageSecondTime(neuroText, music);
    }

    public String addComment(NeuroImageComment imageComment, User user) {
        var neuroText = neuroTextRepository.findByMusicIdAndUser(imageComment.musicId(), user);
        saveUserComment(neuroText, imageComment.text());
        var loadResult = generateImageAndLoadToStorage(imageComment.text());
        if (loadResult instanceof LoadResult.Failed) {
            return DEFAULT_IMAGE_PATH;
        }
        var imagePath = loadResult.fileUuid().path() + loadResult.fileUuid().uuid();
        saveUserPicturePath(neuroText, imagePath);
        return imagePath;
    }

    private LoadResult generateImageAndLoadToStorage(String keyWords) {
        var is = generateImageByKeyWords(keyWords);
        return fileStorage.loadFileToStorage(UploadParams.builder()
                .fileInputStream(is)
                .fileName("generated image")
                .build());
    }

    private String generateImageFirstTime(Music music, User user) {
        var keyWords = music.getAutoKeyWords();
        var loadResult = generateImageAndLoadToStorage(keyWords);
        if (loadResult instanceof LoadResult.Failed) {
            return DEFAULT_IMAGE_PATH;
        }
        var imagePath = loadResult.fileUuid().path() + loadResult.fileUuid().uuid();
        var neuroText = NeuroText.builder()
                .userPicturePath(imagePath)
                .user(user)
                .music(music)
                .build();
        neuroTextRepository.save(neuroText);
        return imagePath;
    }

    private String generateImageSecondTime(NeuroText neuroText, Music music) {
        var keyWords = neuroText.getUserKeyWords() == null ? music.getAutoKeyWords() : neuroText.getUserKeyWords();
        var loadResult = generateImageAndLoadToStorage(keyWords);
        if (loadResult instanceof LoadResult.Failed) {
            return DEFAULT_IMAGE_PATH;
        }
        var imagePath = loadResult.fileUuid().path() + loadResult.fileUuid().uuid();
        saveUserPicturePath(neuroText, imagePath);
        return imagePath;
    }

    // TODO add image generation
    private InputStream generateImageByKeyWords(String keyWords) {
        return InputStream.nullInputStream();
    }

    private void saveUserComment(NeuroText neuroText, String comment) {
        neuroText.setUserKeyWords(comment);
        neuroTextRepository.save(neuroText);
    }

    private void saveUserPicturePath(NeuroText neuroText, String imagePath) {
        neuroText.setUserPicturePath(imagePath);
        neuroTextRepository.save(neuroText);
    }
}