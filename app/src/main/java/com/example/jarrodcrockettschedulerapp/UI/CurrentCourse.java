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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.entities.Assessment;
import com.example.jarrodcrockettschedulerapp.entities.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentCourse extends AppCompatActivity {

    FullRoomDatabase db;
    FloatingActionButton editCourse;
    ListView assessmentListView;
    TextView courseTitleTextView;
    TextView courseStartTextView;
    TextView courseEndTextView;
    TextView instructorNameTextView;
    TextView instructorPhoneTextView;
    TextView instructorEmailTextView;
    TextView noteTextView;
    RadioButton inProgress;
    RadioButton completed;
    RadioButton dropped;
    RadioButton plannedToTake;

    Intent intent;
    int termId;
    int courseId;
    List<Course> courseList;
    Course selectedCourse;
    SimpleDateFormat formatter;
    public static  int numAlert;
    Long startDateLong;
    Long endDateLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_course);

        db = FullRoomDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Course Details");
        courseList = db.courseDao().getAllCourses();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedCourse = db.courseDao().getCourse(termId, courseId);

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        editCourse = (FloatingActionButton) findViewById(R.id.currentCourseEditBtn);

        assessmentListView = findViewById(R.id.currentCourseAssessmentListView);
        courseTitleTextView = findViewById(R.id.currentCourseTitleTB);
        courseStartTextView = findViewById(R.id.currentCourseStartDateTB);
        courseEndTextView = findViewById(R.id.currentCourseEndDateTB);
        instructorNameTextView = findViewById(R.id.currentCourseInstructorNameTB);
        instructorPhoneTextView = findViewById(R.id.currentCourseInstructorPhoneTB);
        instructorEmailTextView = findViewById(R.id.currentCourseInstructorEmailTB);
        noteTextView = findViewById(R.id.currentCourseNote);
        inProgress = (RadioButton) findViewById(R.id.currentCourseInProgressRBtn);
        completed = (RadioButton) findViewById(R.id.currentCourseCompletedRBtn);
        dropped = (RadioButton) findViewById(R.id.currentCourseDroppedRBtn);
        plannedToTake = (RadioButton) findViewById(R.id.currentCoursePlanTTRBtn);


        updateViews();

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCourse.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);

                startActivity(intent);
            }
        });

        assessmentListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), CurrentAssessment.class);
            int assessmentId = db.assessmentDao().getAssessmentList(courseId).get(position).getAssessment_id();
            intent.putExtra("termId", termId);
            intent.putExtra("courseId", courseId);
            intent.putExtra("assessmentId", assessmentId);

            startActivity(intent);
        });


        updateList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.shareBtn) {
            if (noteTextView.getText().toString().matches("")){
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, "There is no course note available for the course.", duration).show();
                return true;
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Course : " + courseTitleTextView.getText().toString() + "\n Note: " + noteTextView.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            return true;

        }

        if (id == R.id.notificationBtn) {
            Intent startDateNotification = new Intent(CurrentCourse.this, MyReceiver.class);
            Intent endDateNotification = new Intent(CurrentCourse.this, MyReceiver.class);
            startDateNotification.putExtra("key", "Course: " + courseTitleTextView.getText().toString() + " has started.");
            endDateNotification.putExtra("key", "Course: " + courseTitleTextView.getText().toString() + " ends today.");
            PendingIntent sender = PendingIntent.getBroadcast(CurrentCourse.this,++numAlert,startDateNotification, 0);
            PendingIntent sender2 = PendingIntent.getBroadcast(CurrentCourse.this,++numAlert,endDateNotification, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            Date startDate = null;
            Date endDate = null;

            try {
                String startDateString = (courseStartTextView.getText().toString());
                startDate = formatter.parse(startDateString);

                String endDateString = (courseEndTextView.getText().toString());
                endDate = formatter.parse(endDateString);

            } catch (ParseException e) {
                e.printStackTrace();

            }
            startDateLong = startDate.getTime();
            endDateLong = endDate.getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, startDateLong, sender);
            alarmManager.set(AlarmManager.RTC_WAKEUP, endDateLong, sender2);

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, "Alerts for start and end have been set for this course.", duration).show();

        }

        return super.onOptionsItemSelected(item);
    }


    private void updateViews() {
        if (selectedCourse != null){
            Date startDate = selectedCourse.getStartDate();
            Date endDate = selectedCourse.getEndDate();

            if (startDate != null && endDate != null){

                String formStartDate = formatter.format(startDate);
                String formEndDate = formatter.format(endDate);

                courseStartTextView.setText(formStartDate);
                courseEndTextView.setText(formEndDate);

            }

            if (selectedCourse.getCourse_title() != null && !selectedCourse.getCourse_title().isEmpty()){
                courseTitleTextView.setText(selectedCourse.getCourse_title());
            }
            if (selectedCourse.getInstructorName() != null && !selectedCourse.getInstructorName().isEmpty()){
                instructorNameTextView.setText(selectedCourse.getInstructorName());
            }
            if (selectedCourse.getInstructorEmail() != null && !selectedCourse.getInstructorEmail().isEmpty()){
                instructorEmailTextView.setText(selectedCourse.getInstructorEmail());
            }
            if (selectedCourse.getInstructorPhone() != null && !selectedCourse.getInstructorPhone().isEmpty()){
                instructorPhoneTextView.setText(selectedCourse.getInstructorPhone());
            }
            if (selectedCourse.getCourse_note() != null && !selectedCourse.getCourse_note().isEmpty()){
                noteTextView.setText(selectedCourse.getCourse_note());
            }
            if (selectedCourse.getCourseStatus() != null && !selectedCourse.getCourseStatus().isEmpty()){
                String courseStatus = selectedCourse.getCourseStatus();
                if (courseStatus.equals("In Progress")) {inProgress.setChecked(true);}
                if (courseStatus.equals("Completed")) {completed.setChecked(true);}
                if (courseStatus.equals("Dropped")) {dropped.setChecked(true);}
                if (courseStatus.equals("Plan To Take")) {plannedToTake.setChecked(true);}
            }
        }else {
            selectedCourse = new Course();
        }
    }

    private void updateList(){
        List<Assessment> allAssessments = new ArrayList<>();
        try {
            allAssessments = db.assessmentDao().getAssessmentList(courseId);
        } catch (Exception e){
            e.printStackTrace();
        }

        String[] items = new String[allAssessments.size()];
        if (!allAssessments.isEmpty()){
            for (int i = 0; i < allAssessments.size(); i++){
                items[i] = allAssessments.get(i).getTitle();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        assessmentListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }


    public void deleteCourse(View view) {
        if (assessmentListView.getAdapter().getCount() != 0){
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, "The assessments must be removed from the course before deleting", duration).show();
            return;
        }

        db.courseDao().deleteCourse(selectedCourse);

        Intent intent = new Intent(getApplicationContext(), CurrentTerm.class);
        intent.putExtra("termId", termId);
        startActivity(intent);
    }

    public void addAssessment(View view) {
        Intent intent = new Intent(CurrentCourse.this, AddAssessment.class);
        intent.putExtra("termId", termId);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    public void backToCurrentTerm(View view) {

        Intent intent = new Intent(CurrentCourse.this, CurrentTerm.class);
        intent.putExtra("termId", termId);
        startActivity(intent);
    }
}