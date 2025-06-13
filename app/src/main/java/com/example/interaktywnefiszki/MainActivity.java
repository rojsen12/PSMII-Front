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
        btnLogout = findViewById(R.id.btnLogout);

        btnFlashcardsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlashcardsList();
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


    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
        editor.remove("token");
        editor.apply();

        Toast.makeText(this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show();

        redirectToLogin();
    }
}