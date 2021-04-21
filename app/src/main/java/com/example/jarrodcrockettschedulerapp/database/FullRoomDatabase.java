package com.example.jarrodcrockettschedulerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.example.jarrodcrockettschedulerapp.DAO.AssessmentDao;
import com.example.jarrodcrockettschedulerapp.DAO.CourseDao;
import com.example.jarrodcrockettschedulerapp.DAO.TermDao;
import com.example.jarrodcrockettschedulerapp.entities.Assessment;
import com.example.jarrodcrockettschedulerapp.entities.Course;
import com.example.jarrodcrockettschedulerapp.entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, exportSchema = false, version = 1)
@TypeConverters({Converters.class})
public abstract class FullRoomDatabase extends RoomDatabase {

    private static final String DB_NAME = "jarrodCrockettSchedulerDB";
    private static FullRoomDatabase instance;

    public static synchronized FullRoomDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), FullRoomDatabase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

}
