package ru.itis.ongakupikature.repository;

import ru.itis.ongakupikature.dto.CatalogSongsInfoDto;

public interface CatalogSongsInfoRepository {

    void loadAuthorsToDb(String name);

    void loadMusicToDb(CatalogSongsInfoDto.SongsInfoDto name);

}
