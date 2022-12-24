package ru.itis.ongakupikature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.itis.ongakupikature.dto.MusicDto;
import ru.itis.ongakupikature.dto.NeuroImageComment;
import ru.itis.ongakupikature.service.GenerateImageService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Epic("Генерация изображения")
class GenerateImageControllerTest extends BaseControllerTest {

    private static final String IMAGE_PATH = "/path/image.png";
    private static final MusicDto MUSIC_DTO = MusicDto.builder()
            .id(1L)
            .name("name")
            .posterPath("path")
            .build();
    private static final NeuroImageComment NEURO_IMAGE_COMMENT = new NeuroImageComment(1L, "comment");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private GenerateImageService generateImageService;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Сгенерировать изображение")
    @Feature("Генерация изображения")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void generateImage() throws Exception {
        given(generateImageService.generateImage(eq(MUSIC_DTO.id()), any())).willReturn(IMAGE_PATH);

        var result = mvc.perform(post("/neuro_image/generate/" + MUSIC_DTO.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(MUSIC_DTO)));

        checkStatusOk(result);
        checkImagePath(result, IMAGE_PATH);
    }

    @Test
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("Добавить комментарий к изображению")
    @Feature("Добавление комментария к изображению")
    @Story("Запрос")
    @WithUserDetails(value = USERNAME, userDetailsServiceBeanName = "customUserDetailsService")
    void addComment() throws Exception {
        given(generateImageService.addComment(eq(NEURO_IMAGE_COMMENT), any())).willReturn(IMAGE_PATH);

        var result = mvc.perform(post("/neuro_image/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(NEURO_IMAGE_COMMENT)));

        checkStatusOk(result);
        checkImagePath(result, IMAGE_PATH);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Путь до изображения совпадет с ожидаемым")
    private void checkImagePath(ResultActions response, String expectedPath) throws Exception {
        response.andExpect(content().string(expectedPath));
    }
}
