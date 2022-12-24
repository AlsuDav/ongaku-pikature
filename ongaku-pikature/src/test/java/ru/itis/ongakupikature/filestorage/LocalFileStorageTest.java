package ru.itis.ongakupikature.filestorage;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.itis.ongakupikature.filestorage.dto.FileUuid;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.filestorage.dto.ReadResult;
import ru.itis.ongakupikature.filestorage.dto.UploadParams;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Epic("Файловое хранилище")
class LocalFileStorageTest {

    private final LocalFileStorage fileStorage = new LocalFileStorage();
    private static final String FOLDER = System.getProperty("user.dir") + "/target/classes/static" + File.separator ;

    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Успешное чтение файла")
    @Feature("Работы с пользовательскими файлами")
    @Story("Чтение файла")
    void readFileFromStorage_success() throws IOException {
        var file = File.createTempFile("file", ".png", new File(FOLDER));
        file.deleteOnExit();
        var readInfo = fileStorage.readFileFromStorage(new FileUuid(file.getName(), "/"));

        checkResultInstanceOf(ReadResult.Success.class, readInfo);
        checkFileContent(readInfo.inputStream());
    }

    @Test
    @DisplayName("Файл не найден")
    @Feature("Работы с пользовательскими файлами")
    @Story("Чтение файла")
    void readFileFromStorage_fileNotFound() throws IOException {
        var readInfo = fileStorage.readFileFromStorage(new FileUuid("nonexistentFile", "/"));

        checkResultInstanceOf(ReadResult.Failed.FileNotRead.class, readInfo);
        resultNull(readInfo.inputStream());
        checkMessage("File with uuid nonexistentFile not found", ((ReadResult.Failed) readInfo).message());
    }

    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Успешная загрузка файла")
    @Feature("Работы с пользовательскими файлами")
    @Story("Загрузка файла")
    void loadFileToStorage_success() throws IOException {
        var bytes = new byte[]{2, 4, 6, 8};
        var inputStream = new ByteArrayInputStream(bytes);
        var loadInfo = fileStorage.loadFileToStorage(
                UploadParams.builder()
                        .fileInputStream(inputStream)
                        .build());

        checkResultInstanceOf(LoadResult.Success.class, loadInfo);
        var fileName = loadInfo.fileUuid().uuid();
        resultNotNull(fileName);
        checkFilename("file", fileName);

        var file = new File(FOLDER+ fileName);
        file.deleteOnExit();
        checkFileContent(bytes, fileName);
    }

    @Step("Проверка типа полученного ответа")
    private static <T> void checkResultInstanceOf(Class<T> expectedType, Object result) {
        assertInstanceOf(expectedType, result);
    }

    @Step("Результат не пустой")
    private static void resultNotNull(Object result) {
        assertNotNull(result);
    }

    @Step("Результат пустой")
    private static void resultNull(Object result) {
        assertNull(result);
    }

    @Step("Проверка текста ошибки")
    private static void checkMessage(String expected, String actual) {
        assertEquals(expected, actual);
    }

    @Step("Проверка префикса названия файла")
    private static void checkFilename(String prefix, String filename) {
        assertTrue(filename.startsWith(prefix));
    }

    @Step("Проверка содержимого файла")
    private static void checkFileContent(byte[] expected, String filename) throws IOException {
        var file = new File(System.getProperty("user.dir") + "/target/classes/static" + File.separator + filename);
        assertArrayEquals(expected, Files.readAllBytes(file.toPath()));
        getBytes(filename);
    }

    @Step("Проверка содержимого файла")
    private static void checkFileContent(InputStream inputStream) throws IOException {
        assertNotNull(inputStream);
        getBytesFromIS(inputStream);
    }

    @Attachment(value = "Вложение", type = "plain/text", fileExtension = ".txt")
    public static byte[] getBytes(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(FOLDER, filename));
    }

    @Attachment(value = "Вложение", type = "plain/text", fileExtension = ".txt")
    public static byte[] getBytesFromIS(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }
}
