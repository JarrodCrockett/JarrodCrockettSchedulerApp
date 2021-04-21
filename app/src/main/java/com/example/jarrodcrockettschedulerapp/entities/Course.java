package com.example.jarrodcrockettschedulerapp.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "course_table",
        foreignKeys = @ForeignKey(
                entity = Term.class,
                parentColumns = "term_id",
                childColumns = "term_id_fk",
                onDelete = CASCADE
        ))
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int course_id;
    @ColumnInfo(name= "term_id_fk")
    private int term_id_fk;
    @ColumnInfo(name = "course_title")
    private String course_title;
    @ColumnInfo(name = "startDate")
    private Date startDate;
    @ColumnInfo(name = "course_end")
    private Date endDate;
    @ColumnInfo(name = "course_status")
    private String courseStatus;
    @ColumnInfo(name = "instructor_name")
    private String instructorName;
    @ColumnInfo(name = "instructor_phone")
    private String instructorPhone;
    @ColumnInfo(name = "instructor_email")
    private String instructorEmail;
    @ColumnInfo(name = "course_note")
    private String course_note;



    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int id) {
        this.course_id = id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public int getTerm_id_fk() {
        return term_id_fk;
    }

    public void setTerm_id_fk(int term_id_fk) {
        this.term_id_fk = term_id_fk;
    }

    public String getCourse_note() {
        return course_note;
    }

    public void setCourse_note(String course_note) {
        this.course_note = course_note;
    }
}
