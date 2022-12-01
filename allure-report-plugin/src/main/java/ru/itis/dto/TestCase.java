package ru.itis.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor()
@Getter
public class TestCase {

    @SerializedName("name")
    private final String name;

    @SerializedName("status")
    private final String status;

    @SerializedName("statusMessage")
    private final String statusMessage;

    private String epic;
    private String feature;
    private String story;

    @SerializedName("labels")
    private final List<LabelValue> labels;

    public void initLabels() {
        if (labels != null) {
            for (LabelValue labelValue : labels) {
                switch (labelValue.getName()) {
                    case "epic" -> epic = labelValue.getValue();
                    case "feature" -> feature = labelValue.getValue();
                    case "story" -> story = labelValue.getValue();
                }
            }
        }
    }
}
