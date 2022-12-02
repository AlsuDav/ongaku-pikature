package ru.itis.ongakupikature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.MusicMoreData;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.UsersRepository;
import ru.itis.ongakupikature.service.MusicService;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MusicControllerTest {

    private static final String USERNAME = "email";
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

    @PostConstruct
    void saveUser() {
        userRepository.save(User.builder()
                .id(1L)
                .login("login")
                .email(USERNAME)
                .password("password")
                .role(User.Role.USER)
                .build());
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UsersRepository userRepository;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private MusicService musicService;

    @Test
    void getAllMusic_shouldReturnAllMusic() throws Exception {
        given(musicService.getAllMusic()).willReturn(MUSIC_DTO_LIST);

        var result = mvc.perform(get("/welcome_page"));

        checkStatusOk(result);
        checkMusicDtoList(result);
    }

    @Test
    void getPlaylistMusic_shouldReturnAllPlaylistMusic() throws Exception {
        given(musicService.getPlaylistMusic(1L)).willReturn(MUSIC_DTO_LIST);

        var result = mvc.perform(get("/login/playlists/1"));

        checkStatusOk(result);
        checkMusicDtoList(result);
    }

    @Test
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void manageLike_shouldReturnOk() throws Exception {
        given(musicService.setLike(any(), any(), anyBoolean())).willReturn(true);

        var result = mvc.perform(post("/song/1/like")
                .param("isLike", "true"));

        checkStatusOk(result);
    }

    @Test
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void manageLike_shouldReturnBadRequest() throws Exception {
        given(musicService.setLike(any(), any(), anyBoolean())).willReturn(false);

        var result = mvc.perform(post("/song/1/like")
                .param("isLike", "true"));

        checkStatusBadRequest(result);
    }

    @Test
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void getMusicMoreData_shouldReturnOk() throws Exception {
        var musicMoreData = new MusicMoreData(true, "path");
        given(musicService.getMusicData(any(), any())).willReturn(musicMoreData);

        var result = mvc.perform(get("/song/1"));

        checkStatusOk(result);
        checkMusicMoreDto(result, musicMoreData);
    }

    // Проверить, что статус ответа успешный (200)
    private void checkStatusOk(ResultActions response) throws Exception {
        response.andExpect(status().isOk());
    }

    // Проверить, что статус ответа неуспешный (400)
    private void checkStatusBadRequest(ResultActions response) throws Exception {
        response.andExpect(status().isBadRequest());
    }

    // Проверить, что лист песен совпадают с ожидаемым
    private void checkMusicDtoList(ResultActions response) throws Exception {
        response.andExpect(content().json(mapper.writeValueAsString(MUSIC_DTO_LIST)));
    }

    // Проверить, что дополнительная информация по песне совпадают с ожидаемой
    private void checkMusicMoreDto(ResultActions response, MusicMoreData expected) throws Exception {
        response.andExpect(content().json(mapper.writeValueAsString(expected)));
    }
}
