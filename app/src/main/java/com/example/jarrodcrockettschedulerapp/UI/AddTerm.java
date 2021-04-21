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
import android.widget.Toast;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.entities.Term;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTerm extends AppCompatActivity {


    FullRoomDatabase db;
    Intent intent;
    int termId;
    Term selectedTerm;
    SimpleDateFormat formatter;
    EditText termNameTextBox;
    EditText termStartDateTextBox;
    EditText termEndDateTextBox;
    Button saveButton;
    List<Term> termList;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myStartDate;
    DatePickerDialog.OnDateSetListener myEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        db = FullRoomDatabase.getInstance(getApplicationContext());
        termList = db.termDao().getTermList();
        intent = getIntent();
        setTitle("Term Details");
        termId = intent.getIntExtra("termId", -1);
        selectedTerm = db.termDao().getTerm(termId);

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        readingTextFields();

        updateView();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Term term : termList){
                    if (term.getTerm_id() == selectedTerm.getTerm_id()){
                        updateTerm();
                        return;
                    }
                }
                if (completionValidation() < 0){
                    return;
                }

                Term newTerm = new Term();
                setTerm(newTerm);

                db.termDao().insertTerm(newTerm);

                Intent intent = new Intent(AddTerm.this, AllTerms.class);

                startActivity(intent);
            }
        });

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

        termStartDateTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTerm.this, myStartDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        termEndDateTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTerm.this, myEndDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateStartLabel(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        termStartDateTextBox.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndLabel(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        termEndDateTextBox.setText(sdf.format(myCalendar.getTime()));
    }

    private void readingTextFields() {
        termNameTextBox = (EditText) findViewById(R.id.addTermNameTB);
        termStartDateTextBox = (EditText) findViewById(R.id.addTermStartTB);
        termEndDateTextBox = (EditText) findViewById(R.id.addTermEndTB);
        saveButton = (Button) findViewById(R.id.saveTermBtn);
    }

    private void updateView(){
        if (selectedTerm != null){
            Date startDate = selectedTerm.getStartDate();
            Date endDate = selectedTerm.getEndDate();

            if (startDate != null && endDate != null){
                String formStartDate = formatter.format(startDate);
                String formEndDate = formatter.format(endDate);

                termStartDateTextBox.setText(formStartDate);
                termEndDateTextBox.setText(formEndDate);
            }

            if (selectedTerm.getTitle() != null && !selectedTerm.getTitle().isEmpty()){
                termNameTextBox.setText(selectedTerm.getTitle());
            }
        } else {
            selectedTerm = new Term();
        }
    }


    public void cancelAddingTerm(View view) {
        Intent intent = new Intent(AddTerm.this, AllTerms.class);
        startActivity(intent);
    }

    private int completionValidation(){
        readingTextFields();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if (termNameTextBox.getText().toString().matches("")){
            Toast.makeText(context, "Term name is empty.", duration).show();
            return -1;
        }
        if (termStartDateTextBox.getText().toString().matches("")){
            Toast.makeText(context, "Term start is empty.", duration).show();
            return -1;
        }
        if (termEndDateTextBox.getText().toString().matches("")){
            Toast.makeText(context, "Term end is empty", duration).show();
            return -1;
        }
        return 1;
    }

    private void setTerm(Term termToUpdate){
        Date startDate = null;
        Date endDate = null;

        try {
            String startDateString = (termStartDateTextBox.getText().toString());
            startDate = formatter.parse(startDateString);

            String endDateString = (termEndDateTextBox.getText().toString());
            endDate = formatter.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        termToUpdate.setTitle(termNameTextBox.getText().toString());
        termToUpdate.setStartDate(startDate);
        termToUpdate.setEndDate(endDate);

    }

    private void updateTerm(){
        if (completionValidation() < 0){
            return;
        }
        setTerm(selectedTerm);
        db.termDao().updateTerm(selectedTerm);
        Intent intent = new Intent(AddTerm.this, AllTerms.class);

        startActivity(intent);
    }

}