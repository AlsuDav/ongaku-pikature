package ru.itis.enums;

import java.util.Objects;

public enum TestStatus {

    IGNORE("unknown", "unknown"),
    FAILED("failed", "<span style=\"color:red;\">failed</span>"),
    BROKEN("broken", "<span style=\"color:darkorange;\">broken</span>"),
    SKIPPED("skipped", "<span style=\"color:dimgray;\">skipped</span>"),
    PASSED("passed", "passed");

    private final String status;
    private final String colorTemplate;

    TestStatus(String status, String colorTemplate) {
        this.status = status;
        this.colorTemplate = colorTemplate;
    }

    public String getColoredStatus() {
        return colorTemplate;
    }

    public static TestStatus fromStatusName(String status) {
        for (TestStatus testStatus : values()) {
            if (Objects.equals(status, testStatus.status)) {
                return testStatus;
            }
        }
        throw new EnumConstantNotPresentException(
                TestStatus.class, "Некорректное значение статуса прохождения теста: %s".formatted(status));
    }
}
