package ru.itis.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LabelValue {

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;
}
