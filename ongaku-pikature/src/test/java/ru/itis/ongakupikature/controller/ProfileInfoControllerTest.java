package ru.itis.ongakupikature.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.entity.User;
import ru.itis.ongakupikature.repository.UsersRepository;
import ru.itis.ongakupikature.service.ProfileService;

import javax.annotation.PostConstruct;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProfileInfoControllerTest {

    private static final MockMultipartFile MULTIPART_FILE = new MockMultipartFile("image", "filename", "image", new byte[]{});

    @PostConstruct
    void saveUser() {
        userRepository.save(User.builder()
                .id(1L)
                .login("login")
                .email("email")
                .password("password")
                .role(User.Role.USER)
                .build());
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UsersRepository userRepository;

    @MockBean
    private ProfileService profileService;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void savePhoto_photoLoaded() throws Exception {
        var saveImageResult = new SaveImageResult.Success();
        given(profileService.saveImage(any(), any())).willReturn(saveImageResult);

        var result = mvc.perform(MockMvcRequestBuilders.multipart("/profile/photo")
                .file(MULTIPART_FILE));

        checkStatusOk(result);
    }

    @Test
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void savePhoto_photoNotLoaded() throws Exception {
        var saveImageResult = new SaveImageResult.Error();
        given(profileService.saveImage(any(), any())).willReturn(saveImageResult);

        var result = mvc.perform(MockMvcRequestBuilders.multipart("/profile/photo")
                .file(MULTIPART_FILE));

        checkStatusBadRequest(result);
    }

    // Проверить, что статус ответа успешный (200)
    private void checkStatusOk(ResultActions response) throws Exception {
        response.andExpect(status().isOk());
    }

    // Проверить, что статус ответа неуспешный (400)
    private void checkStatusBadRequest(ResultActions response) throws Exception {
        response.andExpect(status().isBadRequest());
    }
}
