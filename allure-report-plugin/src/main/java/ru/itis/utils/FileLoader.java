package ru.itis.utils;

import ru.itis.exception.ReadTestCaseFilesException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FileLoader {

    private FileLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Path> getFilesInFolder(String path) {
        try {
            var file = new File(path);
            var paths = new ArrayList<Path>();
            if (file.listFiles() == null) {
                return paths;
            }
            for (var f : Objects.requireNonNull(file.listFiles())) {
                if (f.isFile()) {
                    paths.add(Paths.get(f.getAbsolutePath()));
                }
            }
            return paths;
        } catch (Exception e) {
            throw new ReadTestCaseFilesException("Cannot find files in %s".formatted(path));
        }
    }
}
