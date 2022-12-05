package ru.itis.ongakupikature.filestorage;

import ru.itis.ongakupikature.filestorage.dto.FileUuid;
import ru.itis.ongakupikature.filestorage.dto.LoadResult;
import ru.itis.ongakupikature.filestorage.dto.ReadResult;
import ru.itis.ongakupikature.filestorage.dto.UploadParams;

public interface FileStorage {

    ReadResult readFileFromStorage(FileUuid fileUuid);

    LoadResult loadFileToStorage(UploadParams uploadParams);
}
