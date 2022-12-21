package ru.itis.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.itis.dto.Issue;
import ru.itis.dto.LabelValue;
import ru.itis.dto.TestCase;
import ru.itis.enums.TestStatus;
import ru.itis.exception.ReadTestCaseException;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonHandler {

    private static final String SINGLE_FIELD_TYPE = "SingleEnumIssueCustomField";

    private JsonHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static String serializeIssue(Issue issue) {
        var customFields = new ArrayList<JSONObject>();
        customFields.add(createCustomField("Type", SINGLE_FIELD_TYPE, "Bug"));
        customFields.add(createCustomField("Priority", SINGLE_FIELD_TYPE, "Critical"));

        var projectMap = new HashMap<String, String>();
        projectMap.put("id", issue.getProject().getId());

        var issueMap = new HashMap<String, Object>();
        issueMap.put("project", new JSONObject(projectMap));
        issueMap.put("summary", issue.getSummary());
        issueMap.put("description", issue.getDescription());
        issueMap.put("customFields", customFields);

        var issueObj = new JSONObject(issueMap);

        return issueObj.toJSONString();
    }

    private static JSONObject createCustomField(String name, String fieldType, String value) {
        var valueMap = new HashMap<String, String>();
        valueMap.put("name", value);
        var fieldMap = new HashMap<String, Object>();
        fieldMap.put("$type", fieldType);
        fieldMap.put("name", name);
        fieldMap.put("value", new JSONObject(valueMap));
        return new JSONObject(fieldMap);
    }

    public static TestCase deserializeIssue(String testCaseJson) {
        try {
            JSONParser parser = new JSONParser();
            var obj =  (JSONObject) parser.parse(testCaseJson);
            var array = (JSONArray) obj.get("labels");
            var labels = new ArrayList<LabelValue>();
            for (var o: array) {
                var object = (JSONObject) o;
                labels.add(new LabelValue((String) object.get("name"), (String) object.get("value")));
            }
            var statusName = (String) obj.get("status");
            return new TestCase(
                    (String) obj.get("name"),
                    TestStatus.fromStatusName(statusName),
                    (String) obj.get("statusMessage"),
                    labels
            );
        } catch (ParseException e) {
            throw new ReadTestCaseException(e.getMessage());
        }
    }
}
