package ru.itis.ongakupikature.filestorage.dto;

import lombok.Builder;
import lombok.NonNull;

import java.io.InputStream;

@Builder
public record UploadParams(

        @NonNull InputStream fileInputStream,

        String fileName,

        String userName
) {
}
