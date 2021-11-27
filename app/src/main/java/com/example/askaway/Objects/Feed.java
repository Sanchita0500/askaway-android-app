package com.example.askaway.Objects;

public class Feed {

    private String answer,by,name,question,to;

    public Feed() {
    }

    public Feed(String answer, String by, String name, String question, String to) {
        this.answer = answer;
        this.by = by;
        this.name = name;
        this.question = question;
        this.to = to;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
