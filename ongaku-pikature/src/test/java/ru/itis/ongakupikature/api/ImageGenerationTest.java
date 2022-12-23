package ru.itis.ongakupikature.api;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itis.ongakupikature.controllers.BaseControllerTest;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Генерация изображения")
class ImageGenerationTest extends BaseControllerTest {

    private static final String KEY_WORDS = "любовь море солнце";

    @Autowired
    private ImageGeneration imageGeneration;

    @Test
    @Flaky
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Сгенерировать изображение")
    @Feature("Генерация изображения")
    @Story("API")
    void generateImageByKeyWords_success() throws IOException {
        var is = imageGeneration.generateImageByKeyWords(KEY_WORDS);
        checkInputStream(is);
    }
    @Step("Изображение получено")
    private void checkInputStream(InputStream inputStream) throws IOException {
        assertThat(inputStream)
                .isNotEmpty()
                .isNotEqualTo(InputStream.nullInputStream());
        getBytes(inputStream);
    }

    @Attachment(value = "Полученное изображение", type = "image/png", fileExtension = ".png")
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }
}
