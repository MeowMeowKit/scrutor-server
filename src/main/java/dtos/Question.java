package dtos;

import java.util.ArrayList;
import java.util.UUID;

public class Question {
    private String questionId;
    private String teacherId;
    private String content;
    private String type;
    private int difficulty;
    private ArrayList<Tag> tags;
    private ArrayList<Option> options;
    private int point;

    public Question() {
        this.questionId = null;
        this.teacherId = null;
        this.content = null;
        this.type = null;
        this.difficulty = 0;
        this.tags = new ArrayList<>();
        this.options = new ArrayList<>();
        this.point = 0;
    }

    public Question(String teacherId, String content, String type, int difficulty, ArrayList<Tag> tags, ArrayList<Option> options) {
        this.teacherId = teacherId;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.tags = tags;
        this.options = options;
    }

    public Question(String questionId, String teacherId, String content, String type, int difficulty) {
        this.questionId = questionId;
        this.teacherId = teacherId;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.tags = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    public Question(String questionId, String teacherId, String content, String type, int difficulty, int point) {
        this.questionId = questionId;
        this.teacherId = teacherId;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.point = point;
        this.tags = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    public Question(String content, String type, int difficulty, ArrayList<Tag> tags, ArrayList<Option> options) {
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.tags = tags;
        this.options = options;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean addOption(Option o){
        return this.options.add(o);
    }

    public boolean addTag(Tag t){
        return this.tags.add(t);
    }
}
