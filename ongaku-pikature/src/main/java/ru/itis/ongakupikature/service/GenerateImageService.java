package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.NeuroImageComment;
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

        String keyWords;
        if (neuroText != null && neuroText.getUserKeyWords() != null) {
            keyWords = neuroText.getUserKeyWords();
        } else {
            keyWords = music.getAutoKeyWords();
        }
        var is = generateImageByKeyWords(keyWords);
        var loadResult = fileStorage.loadFileToStorage(UploadParams.builder()
                .fileInputStream(is)
                .fileName("generated image")
                .build());
        if (loadResult instanceof LoadResult.Failed) {
            return DEFAULT_IMAGE_PATH;
        }
        var imagePath = loadResult.fileUuid().path() + loadResult.fileUuid().uuid();
        if (neuroText == null) {
            neuroText = NeuroText.builder()
                    .userPicturePath(imagePath)
                    .user(user)
                    .music(music)
                    .build();
        }
        neuroText.setUserPicturePath(imagePath);
        neuroTextRepository.save(neuroText);
        return imagePath;
    }

    // TODO add image generation
    private InputStream generateImageByKeyWords(String keyWords) {
        return InputStream.nullInputStream();
    }

    public String addComment(NeuroImageComment imageComment, User user) {
        var neuroText = neuroTextRepository.findByMusicIdAndUser(imageComment.musicId(), user);
        neuroText.setUserKeyWords(imageComment.text());
        neuroTextRepository.save(neuroText);
        var is = generateImageByKeyWords(imageComment.text());
        var loadResult = fileStorage.loadFileToStorage(UploadParams.builder()
                .fileInputStream(is)
                .fileName("generated image")
                .build());
        if (loadResult instanceof LoadResult.Failed) {
            return DEFAULT_IMAGE_PATH;
        }
        var imagePath = loadResult.fileUuid().path() + loadResult.fileUuid().uuid();
        neuroText.setUserPicturePath(imagePath);
        neuroTextRepository.save(neuroText);
        return imagePath;
    }
}
