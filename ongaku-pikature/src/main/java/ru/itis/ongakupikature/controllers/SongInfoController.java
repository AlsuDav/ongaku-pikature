package ru.itis.ongakupikature.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.ongakupikature.dto.CatalogSongsInfoDto;
import ru.itis.ongakupikature.service.SongsParserService;

@RestController
@RequiredArgsConstructor
public class SongInfoController {

    private final SongsParserService songsParserService;

    @PostMapping("/loadSongsToDB")
    public ResponseEntity<Void> loadSongsToDB(@RequestBody CatalogSongsInfoDto catalogSongsInfoDto) {

        songsParserService.loadSongsToDb(catalogSongsInfoDto);
        return ResponseEntity.ok().build();

    }
}
