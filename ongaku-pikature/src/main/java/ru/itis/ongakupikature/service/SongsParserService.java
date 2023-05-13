package ru.itis.ongakupikature.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.ongakupikature.dto.CatalogSongsInfoDto;
import ru.itis.ongakupikature.repository.CatalogSongsInfoRepository;

import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class SongsParserService {

    private final CatalogSongsInfoRepository catalogSongsInfoRepository;

    public void loadSongsToDb(CatalogSongsInfoDto catalogSongsInfoDto){
        var authorsSet = new HashSet<>(catalogSongsInfoDto.songsInfoDto().stream().map(CatalogSongsInfoDto.SongsInfoDto::authorName).toList());
        for (var author: authorsSet){
            catalogSongsInfoRepository.loadAuthorsToDb(author);
        }
        for (var song: catalogSongsInfoDto.songsInfoDto()){
            catalogSongsInfoRepository.loadMusicToDb(song);
        }
    }

}
