package ru.itis.ongakupikature.controllers;

import io.qameta.allure.Step;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BaseControllerTest {

    protected static final String USERNAME = "email";
    protected static final User USER = User.builder()
            .id(1L)
            .login("login")
            .email(USERNAME)
            .password("password")
            .role(User.Role.USER)
            .build();

    @MockBean
    protected UsersRepository usersRepository;

    @MockBean
    protected PlaylistRepository playlistRepository;

    @MockBean
    protected PlaylistMusicRepository playlistMusicRepository;

    @MockBean
    protected MusicRepository musicRepository;

    @MockBean
    protected NeuroTextRepository neuroTextRepository;

    @PostConstruct
    void saveUser() {
        Mockito.when(usersRepository.findByEmail(USERNAME))
                .thenReturn(Optional.of(USER));
    }

    @Step("Статус ответа успешный (200)")
    protected static void checkStatusOk(ResultActions response) throws Exception {
        response.andExpect(status().isOk());
    }

    @Step("Статус ответа неуспешный (400)")
    protected static void checkStatusBadRequest(ResultActions response) throws Exception {
        response.andExpect(status().isBadRequest());
    }
}
