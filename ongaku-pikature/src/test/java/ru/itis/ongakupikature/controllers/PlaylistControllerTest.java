package ru.itis.ongakupikature.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.itis.ongakupikature.dto.ActionResult;
import ru.itis.ongakupikature.dto.PlaylistDto;
import ru.itis.ongakupikature.service.PlaylistService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Epic("Плейлист")
class PlaylistControllerTest extends BaseControllerTest {

    private static final List<PlaylistDto> PLAYLIST_DTO_LIST = List.of(
            PlaylistDto.builder()
                    .id(1L)
                    .name("Избранное")
                    .songCount(0)
                    .build(),
            PlaylistDto.builder()
                    .id(2L)
                    .name("Плейлист 1")
                    .songCount(2)
                    .build()
    );

    private static final List<PlaylistDto> ONLY_FAVORITE_PLAYLIST_DTO_LIST = List.of(
            PlaylistDto.builder()
                    .id(1L)
                    .name("Избранное")
                    .songCount(0)
                    .build()
    );

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

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
    @DisplayName("Успешное удаление плейлиста")
    @Feature("Удалить плейлист")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void deletePlaylist_success() throws Exception {
        given(playlistService.deleteUserPlaylist(any(), any())).willReturn(new ActionResult.Success());

        var result = mvc.perform(delete("/login/playlists/1"));

        checkStatusOk(result);
    }

    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Неуспешное удаление плейлиста")
    @Feature("Удалить плейлист")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void deletePlaylist_error() throws Exception {
        given(playlistService.deleteUserPlaylist(any(), any())).willReturn(new ActionResult.Error());

        var result = mvc.perform(delete("/login/playlists/1"));

        checkStatusBadRequest(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное создание плейлиста")
    @Feature("Создать плейлист")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void createPlaylist_success() throws Exception {
        given(playlistService.createUserPlaylist(any(), any())).willReturn(new ActionResult.Success());

        var result = mvc.perform(post("/login/playlists")
                .param("name", "new_playlist"));

        checkStatusOk(result);
    }

    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Неуспешное создание плейлиста")
    @Feature("Создать плейлист")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void createPlaylist_error() throws Exception {
        given(playlistService.createUserPlaylist(any(), any())).willReturn(new ActionResult.Error());

        var result = mvc.perform(post("/login/playlists")
                .param("name", "new_playlist"));

        checkStatusBadRequest(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("У пользователя есть плейлисты")
    @Feature("Получить список плейлистов пользователя")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void getUserPlaylists_userHasPlaylists() throws Exception {
        given(playlistService.getUserPlaylists(any())).willReturn(PLAYLIST_DTO_LIST);

        var result = mvc.perform(get("/login/playlists"));

        checkStatusOk(result);
        checkUserHasPlaylists(result);
    }

    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @DisplayName("Пользователь не создавал плейлисты")
    @Feature("Получить список плейлистов пользователя")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void getUserPlaylists_userHasNotPlaylists() throws Exception {
        given(playlistService.getUserPlaylists(any())).willReturn(ONLY_FAVORITE_PLAYLIST_DTO_LIST);

        var result = mvc.perform(get("/login/playlists"));

        checkStatusOk(result);
        checkUserHasNotPlaylists(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Успешное удаление песни из плейлиста")
    @Feature("Удалить песню из плейлиста")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void deletePlaylistMusic_success() throws Exception {
        given(playlistService.deleteSongFromPlaylist(any(), any(), any())).willReturn(new ActionResult.Success());

        var result = mvc.perform(delete("/login/playlists/1/1"));

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

        var result = mvc.perform(delete("/login/playlists/1/1"));

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

        var result = mvc.perform(post("/login/playlists/1/1"));

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

        var result = mvc.perform(post("/login/playlists/1/1"));

        checkStatusBadRequest(result);
    }

    @Step("У пользователя два плейлиста: \"Избранное\" и \"Плейлист 1\"")
    private void checkUserHasPlaylists(ResultActions response) throws Exception {
        response.andExpect(content().json(mapper.writeValueAsString(PLAYLIST_DTO_LIST)));
    }

    @Step("У пользователя один плейлист по умолчанию: \"Избранное\"")
    private void checkUserHasNotPlaylists(ResultActions response) throws Exception {
        response.andExpect(content().json(mapper.writeValueAsString(ONLY_FAVORITE_PLAYLIST_DTO_LIST)));
    }
}
