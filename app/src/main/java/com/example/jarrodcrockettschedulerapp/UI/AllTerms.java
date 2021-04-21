package com.example.jarrodcrockettschedulerapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.jarrodcrockettschedulerapp.R;
import com.example.jarrodcrockettschedulerapp.database.FullRoomDatabase;
import com.example.jarrodcrockettschedulerapp.entities.Term;

import java.util.ArrayList;
import java.util.List;

public class AllTerms extends AppCompatActivity {

    FullRoomDatabase db;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_terms);
        listView = findViewById(R.id.courseListView);
        db = FullRoomDatabase.getInstance(getApplicationContext());


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), CurrentTerm.class);
            int term_id;
            List<Term> termsList = db.termDao().getTermList();
            term_id = termsList.get(position).getTerm_id();
            intent.putExtra("termId", term_id);

            startActivity(intent);
        });


        updateList();

    }

    private void updateList(){
        List<Term> allTerms = db.termDao().getAllTerms();

        String[] items = new String[allTerms.size()];
        if (!allTerms.isEmpty()){
            for (int i = 0; i < allTerms.size(); i++){
                items[i] = allTerms.get(i).getTitle();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public void addTerm(View view) {
        Intent intent = new Intent(AllTerms.this, AddTerm.class);
        startActivity(intent);
    }
}