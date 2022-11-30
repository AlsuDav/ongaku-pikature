package ru.itis.ongakupikature.allure.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class LabelValue {

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;
}
