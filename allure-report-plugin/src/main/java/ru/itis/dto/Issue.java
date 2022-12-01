package ru.itis.dto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;

import java.util.Optional;

@RequiredArgsConstructor
@AllArgsConstructor
public class Issue {
    private Project project;
    private String summary;
    private String description;

    public Optional<String> toJson() {
        var projectObj = new JSONObject();
        projectObj.put("id", project.id);

        var obj = new JSONObject();
        obj.put("project", projectObj);
        obj.put("summary", summary);
        obj.put("description", description);

        return Optional.ofNullable(obj.toJSONString());
    }
}
