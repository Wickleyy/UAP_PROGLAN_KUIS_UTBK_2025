package org.example.uap_proglan;

import java.util.List;

public class Question {
    private final String text;
    private final List<String> options;
    private final String correctAnswer;
    private final String imagePath;
    private final String explanation;

    public Question(String text, List<String> options, String correctAnswer, String imagePath, String explanation) {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.imagePath = imagePath;
        this.explanation = explanation;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getExplanation() {
        return explanation;
    }
}