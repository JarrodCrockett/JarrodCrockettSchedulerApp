package com.example.jarrodcrockettschedulerapp.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.jarrodcrockettschedulerapp.entities.Assessment;


import java.util.List;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseId ORDER BY assessment_id")
    List<Assessment> getAssessmentList(int courseId);

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseId AND assessment_id = :assessment ORDER BY assessment_id")
    Assessment getAssessment(int courseId, int assessment);

    @Query("SELECT * FROM assessment_table")
    List<Assessment> getAllAssessments();
    @Insert
    void insertAssessment(Assessment assessment);

    @Insert
    void insertAll(Assessment... assessments);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    public void deleteAllAssessmentTable();
}
