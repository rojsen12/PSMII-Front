package com.example.interaktywnefiszki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsListActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ListView lvFlashcards;
    private Button btnBackToMenu;
    private List<String> flashcardSets;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_list);

        tvTitle = findViewById(R.id.tvTitle);
        lvFlashcards = findViewById(R.id.lvFlashcards);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        flashcardSets = new ArrayList<>();
        flashcardSets.add("Zestaw fiszek: Angielski podstawowy");
        flashcardSets.add("Zestaw fiszek: Programowanie Java");
        flashcardSets.add("Zestaw fiszek: Matematyka");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flashcardSets);
        lvFlashcards.setAdapter(adapter);

        lvFlashcards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSet = flashcardSets.get(position);
                Toast.makeText(FlashcardsListActivity.this,
                        "Wybrano: " + selectedSet,
                        Toast.LENGTH_SHORT).show();

                // Intent intent = new Intent(FlashcardsListActivity.this, FlashcardDetailsActivity.class);
                // intent.putExtra("setName", selectedSet);
                // startActivity(intent);
            }
        });

        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}