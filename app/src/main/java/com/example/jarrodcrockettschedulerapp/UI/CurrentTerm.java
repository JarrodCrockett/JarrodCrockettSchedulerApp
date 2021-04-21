package com.example.jarrodcrockettschedulerapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.entities.Course;
import com.example.jarrodcrockettschedulerapp.entities.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentTerm extends AppCompatActivity {

    FullRoomDatabase db;
    ListView courseListView;
    TextView termTitleTextView;
    TextView termStartTextView;
    TextView termEndTextView;
    FloatingActionButton editTerm;
    Intent intent;
    int termId;
    Term selectedTerm;
    SimpleDateFormat formatter;
    Button deleteTerm;
    Button addCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_term);
        db = FullRoomDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Term Details");
        termId = intent.getIntExtra("termId", -1);
        selectedTerm = db.termDao().getTerm(termId);
        formatter = new SimpleDateFormat("MM/dd/yyyy");

        courseListView = findViewById(R.id.courseListView);
        termTitleTextView = findViewById(R.id.termNameCurrentTermTB);
        termStartTextView = findViewById(R.id.currentTermStartTB);
        termEndTextView = findViewById(R.id.currentTermEndTB);
        deleteTerm = (Button) findViewById(R.id.deleteTermBtn);
        addCourse = (Button) findViewById(R.id.addCourse);
        editTerm = (FloatingActionButton) findViewById(R.id.editCurrentTerm);


        updateViews();

        courseListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), CurrentCourse.class);
            int courseId = db.courseDao().getCourseList(termId).get(position).getCourse_id();
            intent.putExtra("termId", termId);
            intent.putExtra("courseId", courseId);

            startActivity(intent);
        });

        deleteTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (courseListView.getAdapter().getCount() != 0){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, "The courses must be removed to delete the term.", duration).show();
                    return;
                }

                db.termDao().deleteTerm(selectedTerm);

                Intent intent = new Intent(getApplicationContext(), AllTerms.class);
                startActivity(intent);
            }
        });

        editTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTerm.class);
                intent.putExtra("termId", termId);

                startActivity(intent);
            }
        });

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentTerm.this, AddCourse.class);
                intent.putExtra("termId", termId);

                startActivity(intent);
            }
        });



        updateList();
    }

    private void updateViews() {
        if (selectedTerm != null){
            Date startDate = selectedTerm.getStartDate();
            Date endDate = selectedTerm.getEndDate();

            String formStartDate = formatter.format(startDate);
            String formEndDate = formatter.format(endDate);

            termTitleTextView.setText(selectedTerm.getTitle());
            termStartTextView.setText(formStartDate);
            termEndTextView.setText(formEndDate);
        }else {
            selectedTerm = new Term();
        }
    }

    private void updateList(){
        List<Course> allCourses = new ArrayList<>();
        try {
            allCourses = db.courseDao().getCourseList(termId);
        }catch (Exception e){
            e.printStackTrace();
        }

        String[] items = new String[allCourses.size()];
        if (!allCourses.isEmpty()){
            for (int i = 0; i < allCourses.size(); i++){
                items[i] = allCourses.get(i).getCourse_title();
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        courseListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public void deleteTerm(View view) {


    }
}