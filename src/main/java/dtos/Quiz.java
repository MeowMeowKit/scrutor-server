package dtos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Quiz {

    private String quizId;
    private String teacherId;
    private String title;
    private String description;
    private Timestamp startAt;
    private Timestamp endAt;
    private Timestamp time;
    private ArrayList<Question> questions = new ArrayList<>();

    public Quiz() {
        this.quizId = null;
        this.teacherId = null;
        this.title = "";
        this.description = "";
        this.startAt = null;
        this.endAt = null;
        this.time = null;
        this.questions = new ArrayList<>();
    }

    public Quiz(String quizId, String teacherId, String title, String description, Timestamp startAt, Timestamp endAt, Timestamp time, ArrayList<Question> questions) {
        this.quizId = quizId;
        this.teacherId = teacherId;
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.time = time;
        this.questions = questions;
    }

    public Quiz(String quizId, String teacherId, String title, String description, Timestamp startAt, Timestamp endAt, Timestamp time) {
        this.quizId = quizId;
        this.teacherId = teacherId;
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.time = time;
        this.questions = new ArrayList<>();
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public boolean addQuestion(Question q) {
        this.questions.add(q);
        return true;
    }
}
