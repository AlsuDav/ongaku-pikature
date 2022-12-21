package ru.itis.ongakupikature.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.ongakupikature.dto.CatalogSongsInfoDto;
import ru.itis.ongakupikature.repository.CatalogSongsInfoRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CatalogSongsInfoRepositoryImpl implements CatalogSongsInfoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String INSERT_MUSIC_QUERY = """
            with insert_music as (
               insert into ongaku_pikature.music (name, text, auto_key_words)
               values (?, ?, ?) on conflict (name) do update
                                   SET name = ?
                                   returning id)
                 insert into ongaku_pikature.author_music(music_id, author_id)
                    values ((select id from insert_music),
                        (select id from ongaku_pikature.author
                            where name like ?))
                        ON CONFLICT (music_id, author_id) DO NOTHING
            """;

    @Transactional
    @Override
    public void loadAuthorsToDb(String name) {
        entityManager.createNativeQuery("insert into ongaku_pikature.author(name) VALUES (?) ON CONFLICT (name) DO NOTHING")
                .setParameter(1, name)
                .executeUpdate();
    }


    @Transactional
    @Override
    public void loadMusicToDb(CatalogSongsInfoDto.SongsInfoDto song) {
        entityManager.createNativeQuery(INSERT_MUSIC_QUERY)
                .setParameter(1, song.name())
                .setParameter(2, song.text())
                .setParameter(3, song.significantWords())
                .setParameter(4, song.name())
                .setParameter(5, song.name())
                .setParameter(6, song.authorName())
                .executeUpdate();
    }
}