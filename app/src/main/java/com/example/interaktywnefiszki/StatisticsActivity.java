package com.example.interaktywnefiszki;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvCreatedFlashcards;
    private TextView tvSolvedFlashcards;
    private TextView tvCorrectAnswers;
    private TextView tvAverageTime;
    private TextView tvMostDifficultSet;
    private Button btnBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tvTitle = findViewById(R.id.tvTitle);
        tvCreatedFlashcards = findViewById(R.id.tvCreatedFlashcards);
        tvSolvedFlashcards = findViewById(R.id.tvSolvedFlashcards);
        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvAverageTime = findViewById(R.id.tvAverageTime);
        tvMostDifficultSet = findViewById(R.id.tvMostDifficultSet);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        loadPlaceholderStatistics();

        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadPlaceholderStatistics() {
        tvCreatedFlashcards.setText("Stworzone fiszki: 42");
        tvSolvedFlashcards.setText("Rozwiązane fiszki: 127");
        tvCorrectAnswers.setText("Poprawność odpowiedzi: 78%");
        tvAverageTime.setText("Średni czas odpowiedzi: 5.2 sekundy");
        tvMostDifficultSet.setText("Najtrudniejszy zestaw: Angielski zaawansowany");
    }
}