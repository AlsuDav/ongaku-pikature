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
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.MusicMoreData;
import ru.itis.ongakupikature.service.MusicService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Epic("Песня")
class MusicControllerTest extends BaseControllerTest {

    private static final List<MusicDto> MUSIC_DTO_LIST = List.of(
            MusicDto.builder()
                    .id(1L)
                    .name("name")
                    .authors(List.of("author1", "author2"))
                    .musicPath("musicPath")
                    .posterPath("posterPath")
                    .build(),
            MusicDto.builder()
                    .id(2L)
                    .name("name2")
                    .authors(List.of("author1"))
                    .musicPath("musicPath")
                    .posterPath("posterPath")
                    .build()
    );

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MusicService musicService;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Получить песни для главной страницы")
    @Feature("Все песни")
    @Story("Запрос")
    void getAllMusic_shouldReturnAllMusic() throws Exception {
        given(musicService.getAllMusic()).willReturn(MUSIC_DTO_LIST);

        var result = mvc.perform(get("/welcome_page"));

        checkStatusOk(result);
        checkMusicDtoList(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Получить песни плейлиста")
    @Feature("Песни плейлиста")
    @Story("Запрос")
    void getPlaylistMusic_shouldReturnAllPlaylistMusic() throws Exception {
        given(musicService.getPlaylistMusic(1L)).willReturn(MUSIC_DTO_LIST);

        var result = mvc.perform(get("/login/playlists/1"));

        checkStatusOk(result);
        checkMusicDtoList(result);
    }

    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Успешный запрос на лайк")
    @Feature("Лайк")
    @Story("Запрос")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void manageLike_shouldReturnOk() throws Exception {
        given(musicService.setLike(any(), any(), anyBoolean())).willReturn(true);

        var result = mvc.perform(post("/song/1/like")
                .param("isLike", "true"));

        verifySetLike(musicService);
        checkStatusOk(result);
    }

    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Неуспешный запрос на лайк")
    @Feature("Лайк")
    @Story("Запрос")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void manageLike_shouldReturnBadRequest() throws Exception {
        given(musicService.setLike(any(), any(), anyBoolean())).willReturn(false);

        var result = mvc.perform(post("/song/1/like")
                .param("isLike", "true"));

        verifySetLike(musicService);
        checkStatusBadRequest(result);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Получить дополнительную информацию по песне")
    @Feature("Дополнительная информация")
    @Story("Запрос")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void getMusicMoreData_shouldReturnOk() throws Exception {
        var musicMoreData = new MusicMoreData(true, "path");
        given(musicService.getMusicData(any(), any())).willReturn(musicMoreData);

        var result = mvc.perform(get("/song/1"));

        checkStatusOk(result);
        checkMusicMoreDto(result, musicMoreData);
    }

    @Step("Лист песен совпадают с ожидаемым")
    private void checkMusicDtoList(ResultActions response) throws Exception {
        response.andExpect(content().json(mapper.writeValueAsString(MUSIC_DTO_LIST)));
    }

    @Step("Дополнительная информация по песне совпадает с ожидаемой")
    private void checkMusicMoreDto(ResultActions response, MusicMoreData musicMoreData) throws Exception {
        response.andExpect(content().json(mapper.writeValueAsString(musicMoreData)));
    }

    @Step("Обработка запроса управления лайками")
    private static void verifySetLike(MusicService musicService) {
        verify(musicService)
                .setLike(USER, 1L, true);
    }
}
