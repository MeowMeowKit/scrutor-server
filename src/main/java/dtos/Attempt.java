package dtos;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Attempt {
    private String attemptId;
    private String studentId;
    private Quiz quiz;
    private double grade;
    private double maxGrade;
    private Timestamp startAt;
    private Timestamp endAt;
    private ArrayList<AttemptQuestion> attemptQuestions;

    public Attempt() {
        this.attemptId = null;
        this.studentId = null;
        this.quiz = null;
        this.grade = 0;
        this.attemptQuestions = new ArrayList<>();
    }

    public Attempt(String attemptId, String studentId, Quiz quiz, double grade, double maxGrade, Timestamp startAt, Timestamp endAt) {
        this.attemptId = attemptId;
        this.studentId = studentId;
        this.quiz = quiz;
        this.grade = grade;
        this.maxGrade = maxGrade;
        this.attemptQuestions = new ArrayList<>();
    }

    public Attempt(String attemptId, String studentId, Quiz quiz, double grade, double maxGrade, ArrayList<AttemptQuestion> attemptQuestions) {
        this.attemptId = attemptId;
        this.studentId = studentId;
        this.quiz = quiz;
        this.grade = grade;
        this.maxGrade = maxGrade;
        this.attemptQuestions = attemptQuestions;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(double maxGrade) {
        this.maxGrade = maxGrade;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
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
