package com.example.jarrodcrockettschedulerapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.database.PopulateDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void createDatabase(View view) {
        PopulateDatabase populateDatabase = new PopulateDatabase();
        populateDatabase.populateDatabase(getApplicationContext());
        FullRoomDatabase db = FullRoomDatabase.getInstance(getApplicationContext());

    }

    public void deleteDatabase(View view) {
        FullRoomDatabase db = FullRoomDatabase.getInstance(getApplicationContext());
        try {
            db.assessmentDao().deleteAllAssessmentTable();
            db.courseDao().deleteAllCourseTable();
            db.termDao().deleteAllTermTable();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadAllTerms(View view) {
        Intent intent = new Intent(MainActivity.this, AllTerms.class);
        startActivity(intent);
    }
}