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

    private JsonHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static String serializeIssue(Issue issue) {
        var projectMap = new HashMap<String, String>();
        projectMap.put("id", issue.getProject().getId());

        var issueMap = new HashMap<String, Object>();
        issueMap.put("project", new JSONObject(projectMap));
        issueMap.put("summary", issue.getSummary());
        issueMap.put("description", issue.getDescription());
        var issueObj = new JSONObject(issueMap);

        return issueObj.toJSONString();
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
