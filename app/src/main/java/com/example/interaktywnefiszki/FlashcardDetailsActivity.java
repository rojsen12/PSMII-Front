package com.example.interaktywnefiszki;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FlashcardDetailsActivity extends AppCompatActivity {

    private TextView tvSetName;
    private Button btnSolveSet;
    private Button btnAddFlashcardToSet;
    private Button btnBackToMainMenu;

    private String currentSetName;
    private int currentSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_details);

        tvSetName = findViewById(R.id.tvSetName);
        btnSolveSet = findViewById(R.id.btnSolveSet);
        btnAddFlashcardToSet = findViewById(R.id.btnAddFlashcardToSet);
        btnBackToMainMenu = findViewById(R.id.btnBackToMainMenu);

        Intent intent = getIntent();
        currentSetId = intent.getIntExtra("setId", -1);
        currentSetName = intent.getStringExtra("setName");

        if (currentSetId == -1) {
            Toast.makeText(this, "Błąd wczytywania szczegółów zestawu.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvSetName.setText(currentSetName);

        btnSolveSet.setOnClickListener(v -> {
            Intent solveIntent = new Intent(FlashcardDetailsActivity.this, SolveFlashcardsActivity.class);
            solveIntent.putExtra("setId", currentSetId);
            solveIntent.putExtra("setName", currentSetName);
            startActivity(solveIntent);
        });

        btnAddFlashcardToSet.setOnClickListener(v -> {
            Intent createIntent = new Intent(FlashcardDetailsActivity.this, CreateFlashcardsActivity.class);
            createIntent.putExtra("setName", currentSetName);
            startActivity(createIntent);
        });

        btnBackToMainMenu.setOnClickListener(v -> {
            Intent mainIntent = new Intent(FlashcardDetailsActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });
    }
}