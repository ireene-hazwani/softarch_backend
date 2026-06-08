/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Course {

    private int courseId;
    private String title;
    private String description;
    private String courseCode;
    private int lecturerId;
public int getLecturerId() {
    return lecturerId;
}

public void setLecturerId(int lecturerId) {
    this.lecturerId = lecturerId;
}
    public Course() {}

    public Course(int courseId, String title,
                  String description,
                  String courseCode) {

        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.courseCode = courseCode;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}