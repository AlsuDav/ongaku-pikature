package ru.itis.ongakupikature.controllers;


import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.service.PlaylistService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Epic("Плейлист")
class PlaylistControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PlaylistService playlistService;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное удаление песни из плейлиста")
    @Feature("Удалить песню из плейлиста")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void deletePlaylistMusic_success() throws Exception {
        given(playlistService.deleteSongFromPlaylist(any(), any(), any())).willReturn(new ActionResult.Success());

        var result = mvc.perform(delete("/login/playlists/1")
                .param("musicId", "1"));

        checkStatusOk(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Неуспешное удаление песни из плейлиста")
    @Feature("Удалить песню из плейлиста")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void deletePlaylistMusic_error() throws Exception {
        given(playlistService.deleteSongFromPlaylist(any(), any(), any())).willReturn(new ActionResult.Error());

        var result = mvc.perform(delete("/login/playlists/1")
                .param("musicId", "1"));

        checkStatusBadRequest(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное добавление песни в плейлист")
    @Feature("Добавить песню в плейлист")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void addSongToPlaylist_success() throws Exception {
        given(playlistService.addSongToPlaylist(any(), any(), any())).willReturn(new ActionResult.Success());

        var result = mvc.perform(post("/login/playlists/1")
                .param("musicId", "1"));

        checkStatusOk(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Неуспешное добавление песни в плейлист")
    @Feature("Добавить песню в плейлист")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void addSongToPlaylist_error() throws Exception {
        given(playlistService.addSongToPlaylist(any(), any(), any())).willReturn(new ActionResult.Error());

        var result = mvc.perform(post("/login/playlists/1")
                .param("musicId", "1"));

        checkStatusBadRequest(result);
    }
}
