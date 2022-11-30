package ru.itis.ongakupikature.allure.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class FileLoader {

    private FileLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Path> getFilesInFolder(String path) throws IOException {
        var pathObject = Paths.get(path);
        try (var paths = Files.walk(pathObject)) {
            return paths
                    .filter(Files::isRegularFile)
                    .toList();
        }
    }
}
