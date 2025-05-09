package com.example.interaktywnefiszki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Button btnFlashcardsList;
    private Button btnCreateFlashcards;
    private Button btnStatistics;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        tvTitle = findViewById(R.id.tvTitle);
        btnFlashcardsList = findViewById(R.id.btnFlashcardsList);
        btnCreateFlashcards = findViewById(R.id.btnCreateFlashcards);
        btnStatistics = findViewById(R.id.btnStatistics);
        btnLogout = findViewById(R.id.btnLogout);

        btnFlashcardsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlashcardsList();
            }
        });

        btnCreateFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateFlashcards();
            }
        });

        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatistics();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        return token != null;
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openFlashcardsList() {
        Intent intent = new Intent(MainActivity.this, FlashcardsListActivity.class);
        startActivity(intent);
    }

    private void openCreateFlashcards() {
        Intent intent = new Intent(MainActivity.this, CreateFlashcardsActivity.class);
        startActivity(intent);
    }

    private void openStatistics() {
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
        editor.remove("token");
        editor.apply();

        Toast.makeText(this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show();

        redirectToLogin();
    }
}