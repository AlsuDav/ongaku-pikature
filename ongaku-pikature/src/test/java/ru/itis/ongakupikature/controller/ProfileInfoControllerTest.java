package ru.itis.ongakupikature.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.itis.ongakupikature.controllers.rest.ProfileInfoController;
import ru.itis.ongakupikature.dto.ProfileInfoDto;
import ru.itis.ongakupikature.dto.SaveImageResult;
import ru.itis.ongakupikature.service.ProfileService;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileInfoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileInfoControllerTest {

    private static final MockMultipartFile MULTIPART_FILE = new MockMultipartFile("image", "filename", "image", new byte[]{});


    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private ProfileService profileService;

    @Test
    void getProfileInfo_shouldReturnCorrectProfileInfo() throws Exception {
        var profileInfo = ProfileInfoDto.builder()
                .id(1L)
                .login("login")
                .photoPath("/photo.img")
                .build();
        given(profileService.getProfileInfo(1L)).willReturn(profileInfo);

        var result = mvc.perform(get("/profile/1"));

        checkStatusOk(result);
        checkProfileInfo(result, profileInfo);
    }

    @Test
    void savePhoto_photoLoaded() throws Exception {
        var saveImageResult = new SaveImageResult.Success();
        given(profileService.saveImage(any(), any())).willReturn(saveImageResult);

        var result = mvc.perform(MockMvcRequestBuilders.multipart("/profile/1")
                .file(MULTIPART_FILE));

        checkStatusOk(result);
    }

    @Test
    void savePhoto_photoNotLoaded() throws Exception {
        var saveImageResult = new SaveImageResult.Error();
        given(profileService.saveImage(any(), any())).willReturn(saveImageResult);

        var result = mvc.perform(MockMvcRequestBuilders.multipart("/profile/1")
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

    // Проверить, что информация по профилю совпадают с ожидаемой
    private void checkProfileInfo(ResultActions response, ProfileInfoDto expected) throws Exception {
        response.andExpect(content().json(mapper.writeValueAsString(expected)));
    }
}
