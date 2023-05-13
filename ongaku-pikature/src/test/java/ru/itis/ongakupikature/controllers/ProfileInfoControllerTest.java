package ru.itis.ongakupikature.controllers;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.service.ProfileService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Epic("Профиль")
class ProfileInfoControllerTest extends BaseControllerTest {

    private static final MockMultipartFile MULTIPART_FILE = new MockMultipartFile("image", "filename", "image", new byte[]{});

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

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
    @Severity(value = SeverityLevel.CRITICAL)
    @Feature("Загрузка фотографии")
    @Story("Запрос")
    @DisplayName("Успешный запрос")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void savePhoto_photoLoaded() throws Exception {
        var saveImageResult = new SaveImageResult.Success();
        given(profileService.saveImage(any(), any())).willReturn(saveImageResult);

        var result = mvc.perform(MockMvcRequestBuilders.multipart("/profile/photo")
                .file(MULTIPART_FILE));

        checkStatusOk(result);
    }

    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @Feature("Загрузка фотографии")
    @Story("Запрос")
    @DisplayName("Неуспешный запрос")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "customUserDetailsService")
    void savePhoto_photoNotLoaded() throws Exception {
        var saveImageResult = new SaveImageResult.Error();
        given(profileService.saveImage(any(), any())).willReturn(saveImageResult);

        var result = mvc.perform(MockMvcRequestBuilders.multipart("/profile/photo")
                .file(MULTIPART_FILE));

        checkStatusBadRequest(result);
    }
}
