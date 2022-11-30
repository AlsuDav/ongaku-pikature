package ru.itis.ongakupikature.allure.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class TestCase {

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    @SerializedName("statusMessage")
    private String statusMessage;

    private String epic;
    private String feature;
    private String story;

    @SerializedName("labels")
    private List<LabelValue> labels;

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
