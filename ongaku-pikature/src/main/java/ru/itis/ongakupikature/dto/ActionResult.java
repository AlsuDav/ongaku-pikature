package ru.itis.ongakupikature.dto;

public sealed interface ActionResult {
    record Success() implements ActionResult {}

    record Error() implements ActionResult {}
}
