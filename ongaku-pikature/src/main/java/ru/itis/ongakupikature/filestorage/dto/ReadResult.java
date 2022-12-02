package ru.itis.ongakupikature.filestorage.dto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public sealed interface ReadResult {

    InputStream inputStream() throws IOException;

    record Success(File file) implements ReadResult {
        @Override
        public InputStream inputStream() throws IOException {
            return Files.newInputStream(file.toPath(), StandardOpenOption.READ);
        }
    }

    sealed interface Failed extends ReadResult {

        String message();

        record FileNotRead(String message) implements Failed {

            @Override
            public InputStream inputStream() {
                return null;
            }
        }
    }
}
