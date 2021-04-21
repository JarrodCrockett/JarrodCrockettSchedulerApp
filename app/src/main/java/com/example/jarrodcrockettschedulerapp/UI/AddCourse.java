package com.example.jarrodcrockettschedulerapp.UI;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.entities.Assessment;
import com.example.jarrodcrockettschedulerapp.entities.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddCourse extends AppCompatActivity {

    FullRoomDatabase db;
    EditText courseTitleTextView;
    EditText courseStartTextView;
    EditText courseEndTextView;
    EditText instructorNameTextView;
    EditText instructorPhoneTextView;
    EditText instructorEmailTextView;
    EditText noteTextView;
    RadioGroup radioGroup;
    RadioButton inProgress;
    RadioButton completed;
    RadioButton dropped;
    RadioButton plannedToTake;
    FloatingActionButton saveCourseBtn;
    Intent intent;
    int termId;
    int courseId;
    List<Course> courseList;
    Course selectedCourse;
    SimpleDateFormat formatter;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myStartDate;
    DatePickerDialog.OnDateSetListener myEndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        db = FullRoomDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Add/Update Course");
        courseList = db.courseDao().getAllCourses();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedCourse = db.courseDao().getCourse(termId, courseId);

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        readingTextFields();

        updateViews();

        myStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateStartLabel();
            }
        };

        myEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateEndLabel();
            }
        };

        courseStartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddCourse.this, myStartDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        courseEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddCourse.this, myEndDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Course course: courseList){
                    if (courseId == course.getCourse_id()){
                        readingTextFields();
                        updateCourse();
                        return;
                    }
                }
                if (completionValidation() < 0){
                    return;
                }
                Course newCourse = new Course();
                readingTextFields();
                setCourse(newCourse);
                courseStatusCheck(newCourse);
                newCourse.setTerm_id_fk(termId);

                db.courseDao().insertCourse(newCourse);

                Intent intent = new Intent(AddCourse.this, CurrentTerm.class);
                intent.putExtra("termId", termId);

                startActivity(intent);
            }
        });

    }

    private void updateStartLabel(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        courseStartTextView.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndLabel(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        courseEndTextView.setText(sdf.format(myCalendar.getTime()));
    }

    private void readingTextFields() {

        courseTitleTextView = (EditText) findViewById(R.id.addCourseTitleTB);
        courseStartTextView = (EditText) findViewById(R.id.addCourseStartDateTB);
        courseEndTextView = (EditText) findViewById(R.id.addCourseEndDateTB);
        instructorNameTextView = (EditText) findViewById(R.id.addCourseInstructorNameTB);
        instructorPhoneTextView = (EditText) findViewById(R.id.addCourseInstructorPhoneTB);
        instructorEmailTextView = (EditText) findViewById(R.id.addCourseInstructorEmailTB);
        noteTextView = (EditText) findViewById(R.id.addCourseNote);
        radioGroup = (RadioGroup) findViewById(R.id.courseStatusRGoup);
        inProgress = (RadioButton) findViewById(R.id.addCourseInProgressRBtn);
        completed = (RadioButton) findViewById(R.id.addCourseCompletedRBtn);
        dropped = (RadioButton) findViewById(R.id.addCourseDroppedRBtn);
        plannedToTake = (RadioButton) findViewById(R.id.addCoursePlanTTRBtn);
        saveCourseBtn = (FloatingActionButton) findViewById(R.id.addCourseSaveButton);

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

    private int completionValidation(){
        readingTextFields();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if (courseTitleTextView.getText().toString().matches("")){
            Toast.makeText(context, "Course title is empty.", duration).show();
            return -1;
        }
        if (courseStartTextView.getText().toString().matches("")){
            Toast.makeText(context, "Course Start is empty.", duration).show();
            return -1;
        }
        if (instructorNameTextView.getText().toString().matches("")){
            Toast.makeText(context, "Instructor name is empty.", duration).show();
            return -1;
        }
        if (radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(context, "The Course does not have a status.", duration).show();
            return -1;
        }
        return 1;
    }

    private void courseStatusCheck(Course course){
        if (completed.isChecked()){
            course.setCourseStatus("Completed");
        }
        if (inProgress.isChecked()){
            course.setCourseStatus("In Progress");
        }
        if (dropped.isChecked()){
            course.setCourseStatus("Dropped");
        }
        if (plannedToTake.isChecked()){
            course.setCourseStatus("Plan To Take");
        }
    }

    private void setCourse(Course courseToUpdate){

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

        courseToUpdate.setCourse_title(courseTitleTextView.getText().toString());
        courseToUpdate.setTerm_id_fk(termId);
        courseStatusCheck(courseToUpdate);
        courseToUpdate.setInstructorName(instructorNameTextView.getText().toString());
        courseToUpdate.setInstructorEmail(instructorEmailTextView.getText().toString());
        courseToUpdate.setInstructorPhone(instructorPhoneTextView.getText().toString());
        courseToUpdate.setCourse_note(noteTextView.getText().toString());
        courseToUpdate.setStartDate(startDate);
        courseToUpdate.setEndDate(endDate);
    }

    private void updateCourse(){
        db = FullRoomDatabase.getInstance(getApplicationContext());
        if (completionValidation() < 0){
            return;
        }
        setCourse(selectedCourse);
        courseStatusCheck(selectedCourse);
        db.courseDao().updateCourse(selectedCourse);
        Intent intent = new Intent(AddCourse.this, CurrentTerm.class);
        intent.putExtra("termId", termId);

        startActivity(intent);
    }

    public void cancelAddCourse(View view) {
        Intent intent = new Intent(AddCourse.this, CurrentTerm.class);
        intent.putExtra("termId", termId);

        startActivity(intent);
    }
}