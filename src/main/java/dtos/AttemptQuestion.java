package dtos;

import java.util.ArrayList;

public class AttemptQuestion {
    private Question question;
    private String fillAnswer;
    private ArrayList<AttemptOption> attemptOptions;

    public AttemptQuestion() {
        this.question = null;
        this.fillAnswer = null;
        this.attemptOptions = new ArrayList<>();
    }

    public AttemptQuestion(Question question, String fillAnswer) {
        this.question = question;
        this.fillAnswer = fillAnswer;
        this.attemptOptions = new ArrayList<>();
    }

    public AttemptQuestion(Question question, String fillAnswer, ArrayList<AttemptOption> attemptOptions) {
        this.question = question;
        this.fillAnswer = fillAnswer;
        this.attemptOptions = attemptOptions;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getFillAnswer() {
        return fillAnswer;
    }

    public void setFillAnswer(String fillAnswer) {
        this.fillAnswer = fillAnswer;
    }

    public ArrayList<AttemptOption> getAttemptOptions() {
        return attemptOptions;
    }

    public void setAttemptOptions(ArrayList<AttemptOption> attemptOptions) {
        this.attemptOptions = attemptOptions;
    }

    public boolean addAttemptOption(AttemptOption ao) {
        this.attemptOptions.add(ao);
        return true;
    }
}
