package com.example.jarrodcrockettschedulerapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.entities.Assessment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAssessment extends AppCompatActivity {

    FullRoomDatabase db;
    EditText assessmentTitleTextBox;
    EditText assessmentDateTextBox;
    RadioGroup radioGroup;
    RadioButton objectiveAssessment;
    RadioButton performanceAssessment;
    List<Assessment> assessmentList;
    Intent intent;
    int termId;
    int courseId;
    int assessmentId;
    Assessment selectedAssessment;
    SimpleDateFormat formatter;
    Button saveBtn;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        db = FullRoomDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Assessment Details");
        assessmentList = db.assessmentDao().getAllAssessments();
        assessmentId = intent.getIntExtra("assessmentId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedAssessment = db.assessmentDao().getAssessment(courseId, assessmentId);

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        readingTextFields();

        updateView();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Assessment assessment: assessmentList){
                    if (assessmentId == assessment.getAssessment_id()){
                        updateAssessment();
                        return;
                    }
                }
                if (completionValidation() < 0){
                    return;
                }
                System.out.println("Course ID =================>  " + courseId);
                Assessment newAssessment = new Assessment();
                setAssessment(newAssessment);
                assessmentTypeCheck(newAssessment);
                newAssessment.setCourse_id_fk(courseId);

                db.assessmentDao().insertAssessment(newAssessment);

                Intent intent = new Intent(AddAssessment.this, CurrentCourse.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);

                startActivity(intent);

            }
        });

         myDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateDateLabel();
            }
        };

        assessmentDateTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddAssessment.this, myDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateLabel(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        assessmentDateTextBox.setText(sdf.format(myCalendar.getTime()));
    }

    private void readingTextFields() {
        assessmentTitleTextBox = (EditText) findViewById(R.id.addAssessmentTitleTB);
        assessmentDateTextBox = (EditText) findViewById(R.id.addAssessmentDate);
        radioGroup = (RadioGroup) findViewById(R.id.assessmentTypeRBGroup);
        objectiveAssessment = (RadioButton) findViewById(R.id.addAssessmentObjectiveRBtn);
        performanceAssessment = (RadioButton) findViewById(R.id.addAssessmentPerformanceRBtn);
        saveBtn = (Button) findViewById(R.id.addAssessmentSaveBtn);
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

    private int completionValidation(){
        readingTextFields();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if (assessmentTitleTextBox.getText().toString().matches("")){
           Toast.makeText(context, "The assessment title is missing", duration).show();
            return -1;
        }
        if (assessmentDateTextBox.getText().toString().matches("")) {
            Toast.makeText(context, "Assessment date field is empty", duration).show();
            return -1;
        }
        if (radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(context, "The Assessment type is not selected", duration).show();
            return -1;
        }
        return 1;
    }

    private void assessmentTypeCheck(Assessment assessment){
        if (objectiveAssessment.isChecked()){
            assessment.setAssessmentType("Objective");
        }
        if (performanceAssessment.isChecked()){
            assessment.setAssessmentType("Performance");
        }
    }

    private void setAssessment(Assessment assessmentToUpdate){
        Date assessmentDate = null;

        try {
            String assessmentDateSting = (assessmentDateTextBox.getText().toString());
            assessmentDate = formatter.parse(assessmentDateSting);
            assessmentToUpdate.setAssessmentDate(assessmentDate);
        } catch (ParseException e){
            e.printStackTrace();
        }

        assessmentToUpdate.setTitle(assessmentTitleTextBox.getText().toString());
        assessmentToUpdate.setCourse_id_fk(courseId);
    }

    private void updateAssessment(){
        if (completionValidation() < 0){
            return;
        }
        setAssessment(selectedAssessment);
        assessmentTypeCheck(selectedAssessment);
        db.assessmentDao().updateAssessment(selectedAssessment);
        Intent intent = new Intent(AddAssessment.this, CurrentCourse.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("termId", termId);

        startActivity(intent);
    }

    public void loadCurrentCourse(View view) {

        Intent intent = new Intent(getApplicationContext(), CurrentCourse.class);
        intent.putExtra("termId", termId);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }
}