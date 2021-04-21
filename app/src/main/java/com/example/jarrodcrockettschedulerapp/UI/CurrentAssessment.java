package com.example.jarrodcrockettschedulerapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.entities.Assessment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CurrentAssessment extends AppCompatActivity {

    FullRoomDatabase db;
    TextView assessmentTitleTextBox;
    TextView assessmentDateTextBox;
    RadioButton objectiveAssessment;
    RadioButton performanceAssessment;
    List<Assessment> assessmentList;
    Intent intent;
    int termId;
    int courseId;
    int assessmentId;
    Assessment selectedAssessment;
    SimpleDateFormat formatter;
    FloatingActionButton editBtn;
    Long assessmentDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_assessment);
        db = FullRoomDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Assessment Details");
        assessmentList = db.assessmentDao().getAllAssessments();
        assessmentId = intent.getIntExtra("assessmentId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedAssessment = db.assessmentDao().getAssessment(courseId, assessmentId);

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        assessmentTitleTextBox = findViewById(R.id.currentAssessmentTitleTB);
        assessmentDateTextBox = findViewById(R.id.currentAssessmentDate);
        objectiveAssessment = findViewById(R.id.currentAssessmentObjectiveRBtn);
        performanceAssessment = findViewById(R.id.currentAssessmentPerformanceRBtn);
        editBtn = (FloatingActionButton) findViewById(R.id.currentAssessmentEditBtn);

        updateView();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("term Id = " + termId  + " Course Id = " + courseId + "AssessmentId = " + assessmentId);
                Intent intent = new Intent(getApplicationContext(), AddAssessment.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);

                startActivity(intent);
            }
        });

    }
    private void updateView(){
        if (selectedAssessment != null) {
            Date assessmentDate = selectedAssessment.getAssessmentDate();

            if (assessmentDate != null) {
                String formAssessmentDate = formatter.format(assessmentDate);

                assessmentDateTextBox.setText(formAssessmentDate);
            }
            if (selectedAssessment.getTitle() != null && !selectedAssessment.getTitle().isEmpty()){
                assessmentTitleTextBox.setText(selectedAssessment.getTitle());
            }
            if (selectedAssessment.getAssessmentType() != null && !selectedAssessment.getAssessmentType().isEmpty()){
                String assessmentType = selectedAssessment.getAssessmentType();
                if (assessmentType.equals("Objective")) {objectiveAssessment.setChecked(true);}
                if (assessmentType.equals("Performance")) {performanceAssessment.setChecked(true);}
            }
        } else {
            selectedAssessment = new Assessment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.assessmentNotificationBtn) {
            Intent startDateNotification = new Intent(CurrentAssessment.this, MyReceiver.class);
            startDateNotification.putExtra("key", "Assessment: " + assessmentTitleTextBox.getText().toString() + " has started.");
            PendingIntent sender = PendingIntent.getBroadcast(CurrentAssessment.this,++CurrentCourse.numAlert,startDateNotification, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            Date startDate = null;

            try {
                String startDateString = (assessmentDateTextBox.getText().toString());
                startDate = formatter.parse(startDateString);

            } catch (ParseException e) {
                e.printStackTrace();

            }
            assessmentDueDate = startDate.getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, assessmentDueDate, sender);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, "Assessment due date notification is set for this assessment.", duration).show();

        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteAssessment(View view) {
        db.assessmentDao().deleteAssessment(selectedAssessment);

        Intent intent = new Intent(getApplicationContext(), CurrentCourse.class);
        intent.putExtra("termId", termId);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    public void backToCurrentCourse(View view) {
        Intent intent = new Intent(getApplicationContext(), CurrentCourse.class);
        intent.putExtra("termId", termId);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }
}