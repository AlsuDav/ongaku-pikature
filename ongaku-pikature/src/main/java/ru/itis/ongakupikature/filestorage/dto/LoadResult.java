package ru.itis.ongakupikature.filestorage.dto;

public sealed interface LoadResult {

    FileUuid fileUuid();

    record Success(FileUuid fileUuid) implements LoadResult {
    }

    sealed interface Failed extends LoadResult {

        record FileNotLoaded(String message) implements Failed {
            @Override
            public FileUuid fileUuid() {
                return null;
            }
        }
    }

}
