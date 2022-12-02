package ru.itis.ongakupikature.dto;

public sealed interface SaveImageResult {

    record Success() implements SaveImageResult {}

    record Error() implements SaveImageResult {}
}
