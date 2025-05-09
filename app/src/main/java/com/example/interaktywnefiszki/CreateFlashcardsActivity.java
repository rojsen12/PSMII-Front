package com.example.interaktywnefiszki;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateFlashcardsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private EditText etSetName;
    private EditText etFrontSide;
    private EditText etBackSide;
    private Button btnAddFlashcard;
    private Button btnBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcards);

        tvTitle = findViewById(R.id.tvTitle);
        etSetName = findViewById(R.id.etSetName);
        etFrontSide = findViewById(R.id.etFrontSide);
        etBackSide = findViewById(R.id.etBackSide);
        btnAddFlashcard = findViewById(R.id.btnAddFlashcard);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        btnAddFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = etSetName.getText().toString().trim();
                String front = etFrontSide.getText().toString().trim();
                String back = etBackSide.getText().toString().trim();

                if (setName.isEmpty() || front.isEmpty() || back.isEmpty()) {
                    Toast.makeText(CreateFlashcardsActivity.this,
                            "Wypełnij wszystkie pola",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateFlashcardsActivity.this,
                            "Fiszka dodana do zestawu: " + setName,
                            Toast.LENGTH_SHORT).show();

                    etFrontSide.setText("");
                    etBackSide.setText("");
                    etFrontSide.requestFocus();
                }
            }
        });

        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Zamyka aktualną aktywność i wraca do poprzedniej (MainActivity)
            }
        });
    }
}