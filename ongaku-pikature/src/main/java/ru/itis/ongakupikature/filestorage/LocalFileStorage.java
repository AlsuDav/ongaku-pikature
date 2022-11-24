package ru.itis.ongakupikature.filestorage;

import lombok.extern.slf4j.Slf4j;
import ru.itis.ongakupikature.filestorage.dto.FileUuid;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.filestorage.dto.ReadResult;
import ru.itis.ongakupikature.filestorage.dto.UploadParams;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
public class LocalFileStorage implements FileStorage {

    private static final String FOLDER = System.getProperty("java.io.tmpdir") + File.separator;

    @Override
    public ReadResult readFileFromStorage(FileUuid fileUuid) {
        var file = new File(FOLDER + fileUuid.uuid());
        if (file.exists()) {
            return new ReadResult.Success(file);
        }
        var message = "File with uuid %s not found".formatted(fileUuid.uuid());
        log.warn(message);
        return new ReadResult.Failed.FileNotRead(message);
    }

    @Override
    public LoadResult loadFileToStorage(UploadParams uploadParams) {
        try (var is = uploadParams.fileInputStream()) {
            var file = File.createTempFile("file", "");
            file.deleteOnExit();
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return new LoadResult.Success(new FileUuid(file.getName()));
        } catch (IOException e) {
            log.warn("File not loaded. Filename: {}, username: {}", uploadParams.fileName(), uploadParams.userName(), e);
            return new LoadResult.Failed.FileNotLoaded(e.getMessage());
        }
    }
}
