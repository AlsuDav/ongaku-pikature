package ru.itis.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.itis.dto.LabelValue;
import ru.itis.dto.TestCase;
import ru.itis.exception.ReadTestCaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AllureReportParser implements TestRepostParser {

    private static final String SUCCESS_STATUS = "passed";

    @Override
    public List<TestCase> readTestCases(List<Path> paths) {
        List<TestCase> testCases = new ArrayList<>();
        TestCase testCase;

        for (Path path : paths) {
            testCase = readTestCaseJson(path.toString());

            if (!testCase.getStatus().equals(SUCCESS_STATUS) ) {
                testCase.initLabels();
                testCases.add(testCase);
            }
        }

        return testCases;
    }

    private TestCase readTestCaseJson(String path) {
        try {
            String text = new String(Files.readAllBytes(Paths.get(path)));
            JSONParser parser = new JSONParser();
            var obj =  (JSONObject) parser.parse(text);
            var array = (JSONArray) obj.get("labels");
            var labels = new ArrayList<LabelValue>();
            for (var o: array) {
                var object = (JSONObject) o;
                labels.add(new LabelValue((String) object.get("name"), (String) object.get("value")));
            }
            var testCase = new TestCase(
                    (String) obj.get("name"),
                    (String) obj.get("status"),
                    (String) obj.get("statusMessage"),
                    labels
            );
            testCase.initLabels();
            return testCase;
        } catch (IOException | ParseException e) {
            throw new ReadTestCaseException(e.getMessage());
        }
    }
}
