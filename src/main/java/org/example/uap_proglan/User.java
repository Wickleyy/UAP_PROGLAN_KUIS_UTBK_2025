package org.example.uap_proglan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private Map<String, List<String>> answerHistoryBySubtest = new HashMap<>();
    private Map<String, Integer> scoresBySubtest = new HashMap<>();
    private List<String> results = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, List<String>> getAnswerHistoryBySubtest() {
        return answerHistoryBySubtest;
    }

    public Map<String, Integer> getScoresBySubtest() {
        return scoresBySubtest;
    }

    public List<String> getResults() {
        return results;
    }

    public void addResult(String result) {
        results.add(result);
    }
}
