package ru.itis.ongakupikature.repository;

import org.springframework.stereotype.Repository;
import ru.itis.ongakupikature.dto.CatalogSongsInfoDto;

@Repository
public interface CatalogSongsInfoRepository {

    void loadAuthorsToDb(String name);

    void loadMusicToDb(CatalogSongsInfoDto.SongsInfoDto name);

}
