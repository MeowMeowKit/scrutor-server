package dtos;

import java.util.ArrayList;

public class AttemptQuestion {
    private String attemptQuestionId;
    private Question question;
    private String fillAnswer;
    private ArrayList<AttemptOption> attemptOptions;

    public AttemptQuestion() {
        this.question = null;
        this.fillAnswer = null;
        this.attemptOptions = new ArrayList<>();
    }

    public AttemptQuestion(String attemptQuestionId, Question question, String fillAnswer) {
        this.attemptQuestionId = attemptQuestionId;
        this.question = question;
        this.fillAnswer = fillAnswer;
        this.attemptOptions = new ArrayList<>();
    }

    public AttemptQuestion(String attemptQuestionId, Question question, String fillAnswer, ArrayList<AttemptOption> attemptOptions) {
        this.attemptQuestionId = attemptQuestionId;
        this.question = question;
        this.fillAnswer = fillAnswer;
        this.attemptOptions = attemptOptions;
    }

    public String getAttemptQuestionId() {
        return attemptQuestionId;
    }

    public void setAttemptQuestionId(String attemptQuestionId) {
        this.attemptQuestionId = attemptQuestionId;
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
