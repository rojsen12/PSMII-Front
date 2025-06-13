package com.example.interaktywnefiszki;

import android.app.Activity;
import android.content.Intent;
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
    private Button btnSaveAndFinish;

    private FlashcardRepository repository;
    private String initialSetName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcards);

        tvTitle = findViewById(R.id.tvTitle);
        etSetName = findViewById(R.id.etSetName);
        etFrontSide = findViewById(R.id.etFrontSide);
        etBackSide = findViewById(R.id.etBackSide);
        btnAddFlashcard = findViewById(R.id.btnAddFlashcard);
        btnSaveAndFinish = findViewById(R.id.btnSaveAndFinish);

        repository = new FlashcardRepository(getApplication());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("setName")) {
            initialSetName = intent.getStringExtra("setName");
            etSetName.setText(initialSetName);
            etSetName.setEnabled(false);
            tvTitle.setText("Dodaj fiszkę do zestawu");
        } else {
            tvTitle.setText("Utwórz nowy zestaw fiszek");
        }

        btnAddFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = etSetName.getText().toString().trim();
                String front = etFrontSide.getText().toString().trim();
                String back = etBackSide.getText().toString().trim();

                if (setName.isEmpty() || front.isEmpty() || back.isEmpty()) {
                    Toast.makeText(CreateFlashcardsActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                } else {
                    repository.addFlashcardToSet(setName, front, back);

                    Toast.makeText(CreateFlashcardsActivity.this, "Fiszka dodana!", Toast.LENGTH_SHORT).show();

                    etFrontSide.setText("");
                    etBackSide.setText("");
                    etFrontSide.requestFocus();
                }
            }
        });

        btnSaveAndFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalSetName = etSetName.getText().toString().trim();

                if (initialSetName == null) {
                    if (finalSetName.isEmpty()) {
                        Toast.makeText(CreateFlashcardsActivity.this, "Nazwa zestawu nie może być pusta", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    repository.addSet(finalSetName, new FlashcardRepository.RepositoryCallback<Long>() {
                        @Override
                        public void onComplete(Long result) {
                            runOnUiThread(() -> {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("newSetName", finalSetName);
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            });
                        }
                    });
                } else {
                    finish();
                }
            }
        });
    }
}