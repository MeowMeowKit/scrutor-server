package dtos;

import java.util.ArrayList;

public class Class {
    private String classId;
    private String teacherId;
    private String name;
    private ArrayList<User> students;

    public Class(String classId, String teacherId, String name, ArrayList<User> students) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.name = name;
        this.students = students;
    }

    public Class(String classId, String teacherId, String name) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.name = name;
    }


    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<User> students) {
        this.students = students;
    }
}
