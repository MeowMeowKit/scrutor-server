package dtos;

import java.util.ArrayList;

public class Attempt {
    private String attemptId;
    private String userId;
    private String quizId;
    private double grade;
    private ArrayList<AttemptQuestion> attemptQuestions;

    public Attempt() {
        this.attemptId = null;
        this.userId = null;
        this.quizId = null;
        this.grade = 0;
        this.attemptQuestions = new ArrayList<>();
    }

    public Attempt(String attemptId, String userId, String quizId, double grade) {
        this.attemptId = attemptId;
        this.userId = userId;
        this.quizId = quizId;
        this.grade = grade;
        this.attemptQuestions = new ArrayList<>();
    }

    public Attempt(String attemptId, String userId, String quizId, double grade, ArrayList<AttemptQuestion> attemptQuestions) {
        this.attemptId = attemptId;
        this.userId = userId;
        this.quizId = quizId;
        this.grade = grade;
        this.attemptQuestions = attemptQuestions;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public ArrayList<AttemptQuestion> getAttemptQuestions() {
        return attemptQuestions;
    }

    public void setAttemptQuestions(ArrayList<AttemptQuestion> attemptQuestions) {
        this.attemptQuestions = attemptQuestions;
    }

    public boolean addAttempQuestion(AttemptQuestion aq) {
        this.attemptQuestions.add(aq);
        return true;
    }
}
