package com.example.jarrodcrockettschedulerapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.jarrodcrockettschedulerapp.entities.Course;

import java.util.List;
@Dao
public interface CourseDao {

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termId ORDER BY course_id")
    List<Course> getCourseList(int termId);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termId  AND course_id = :courseId ORDER BY course_id")
    Course getCourse(int termId, int courseId);

    @Query("SELECT * FROM course_table")
    List<Course> getAllCourses();

    @Insert
    void insertCourse(Course course);

    @Insert
    void insertAll(Course... courses);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM course_table")
    public void deleteAllCourseTable();

}
